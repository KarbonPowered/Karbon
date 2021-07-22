package com.karbonpowered.engine.util.collection.map

import kotlinx.atomicfu.AtomicLongArray

/**
 * An atomic HashMap that maps integers to positive short values
 *
 * Once a key value pair is set, it cannot be changed again
 */
class AtomicIntShortSingleUseHashMap(
    val size: Int
) {
    private val array = AtomicLongArray(size).apply {
        repeat(size) {
            get(it).value = EMPTY_ENTRY
        }
    }

    operator fun get(key: Int): Short {
        val hashed = key.hash()
        var index = hashed
        var probedEntry: Long
        var empty: Boolean
        while (true) {
            probedEntry = array[index].value
            empty = probedEntry.isEmpty()
            if (empty || probedEntry.key() == key) {
                break
            }
            index = (index + 1) % size
            if (index == hashed) {
                return EMPTY_VALUE
            }
        }
        return if (!empty) {
            probedEntry.toShort()
        } else {
            EMPTY_VALUE
        }
    }

    fun putIfAbsent(key: Int, value: Short): Short {
        val hashed = key.hash()
        var index = hashed
        var probedEntry = 0L
        var entrySet: Boolean
        while (true) {
            entrySet = setEntry(index, key, value)
            if (entrySet) {
                break
            }
            probedEntry = array[index].value
            if (probedEntry.key() == key) {
                break
            }
            index = (index + 1) % size
            check(index != hashed) { "Map is full" }
        }
        return if (entrySet) {
            EMPTY_VALUE
        } else {
            probedEntry.toShort()
        }
    }

    private fun setEntry(index: Int, key: Int, value: Short): Boolean =
        array[index].compareAndSet(EMPTY_ENTRY, pack(key, value))

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Int.hash(): Int {
        var result = this
        result = result xor (result ushr 20 xor (result ushr 12))
        result = result xor (result ushr 7) xor (result ushr 4)
        result = (result and 0x7FFFFFFF) % size
        return result
    }

    fun isEmptyValue(value: Short) = value == EMPTY_VALUE

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Long.isEmpty() = this == EMPTY_ENTRY

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Long.key(): Int = (this shr 16).toInt()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun pack(key: Int, value: Short): Long =
        ((key.toLong() and 0xFFFFFFFFL) shl 16) or (value.toLong() and 0xFFFFL)

    companion object {
        private const val EMPTY_VALUE: Short = -1
        private const val EMPTY_ENTRY = -0x1000000000000L
    }
}
