package com.karbonpowered.common.collection.concurrent

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

/**
 * A 3d int based Object map that is backed by AtomicReferenceArrays arranged in a
 * tree structure.
 *
 * The Map operates in a tree structure. The bits variable indicates the number of
 * bits used per level of the tree. This is used to mask the input coordinates.
 *
 * The length of the internal arrays are determined by the bits parameter.
 *
 * If bits is set to 4, then each coordinate provides 4 bits for the array index.
 * That gives a total array length of 16 * 16 * 16 = 4096.
 *
 * Inserting a new object with a random key would probably require new arrays to be
 * created for the entire depth of the tree.
 *
 * A given depth can be guaranteed by keeping ensuring that all elements are within a
 * cube that has an edge of 2 ^ (depth * bits) or smaller. Increasing the bits
 * variable reduces the depth of the internal tree at the expense of more memory used
 * per array.
 *
 * The map is thread-safe. Map update operations can not be carried out by more than
 * one thread at the same time. However, read operations are concurrent.
 *
 * The map is optimised for use where all the coordinates occur in a small number of
 * contiguous cuboids.
 *
 * @param T the value type
 */
class TripleIntObjectReferenceArrayMap<T>(
    val bits: Int,
    val depth: Int = 1
) : TripleIntObjectMap<T> {
    override val values: MutableCollection<T> = LinkedHashSet()
    private val lock = reentrantLock()
    private val doubleBits = bits shl 1
    private val width = 1 shl bits
    private val arraySize = width * width * width
    private val bitMask = width - 1
    private val root = atomic<Entry<T>>(AtomicReferenceArrayEntry(depth))
    private val valuesSnapshot = atomic<Collection<T>?>(null)
    private var removed = 0

    override fun get(x: Int, y: Int, z: Int): T? =
        getEntryRaw(x, y, z)?.value

    override fun remove(x: Int, y: Int, z: Int): T? = lock.withLock {
        val value = getEntryRaw(x, y, z)?.remove()
        if (value != null) {
            valuesSnapshot.value = null
            removed++
            if (!values.remove(value)) {
                throw IllegalStateException("Item removed from map was not in item set")
            }
        }
        return value
    }

    override fun remove(x: Int, y: Int, z: Int, value: T): Boolean = lock.withLock {
        val entry = getEntryRaw(x, y, z)
        if (entry != null) {
            val remove = entry.remove(value)
            if (remove) {
                valuesSnapshot.value = null
                removed++
                if (!values.remove(value)) {
                    throw IllegalStateException("Item removed from map was not in item set")
                }
            }
            remove
        } else {
            false
        }
    }
    override fun getOrPut(x: Int, y: Int, z: Int, value: () -> T): T = lock.withLock {
        TODO("Not yet implemented")
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getEntryRaw(x: Int, y: Int, z: Int): Entry<T>? {
        var entry = root.value
        var shift = entry.initialShift
        repeat(entry.depth) {
            entry = entry.getSubEntry(x, y, z, shift) ?: return null
            shift -= bits
        }
        return if (entry.testKey(x, y, z)) {
            entry
        } else {
            null
        }
    }

    interface Entry<T> {
        val value: T
        val depth: Int
        val initialShift: Int

        fun getSubEntry(x: Int, y: Int, z: Int, shift: Int): Entry<T>?

        fun testKey(x: Int, y: Int, z: Int): Boolean

        fun remove(): T?

        fun remove(value: T): Boolean

        fun put(value: T): T?

        fun getOrPut(value: () -> T): T
    }

    private inner class AtomicReferenceArrayEntry<T>(
        override val depth: Int
    ) : Entry<T> {
        val array = atomicArrayOfNulls<Entry<T>>(arraySize)
        override val value: T
            get() = throw UnsupportedOperationException("${this::class.simpleName} cannot store values directly")
        override val initialShift: Int = depth * bits

        override fun getSubEntry(x: Int, y: Int, z: Int, shift: Int) =
            array[index(x, y, z, shift)].value

        override fun testKey(x: Int, y: Int, z: Int): Boolean =
            throw UnsupportedOperationException("${this::class.simpleName} does not contain key/value pairs")

        override fun remove(): T =
            throw UnsupportedOperationException("${this::class.simpleName} does not contain key/value pairs")

        override fun remove(value: T): Boolean =
            throw UnsupportedOperationException("${this::class.simpleName} does not contain key/value pairs")

        override fun put(value: T): T =
            throw UnsupportedOperationException("${this::class.simpleName} does not contain key/value pairs")

        override fun getOrPut(value: () -> T): T =
            throw UnsupportedOperationException("${this::class.simpleName} does not contain key/value pairs")

        operator fun set(x: Int, y: Int, z: Int, shift: Int, subEntry: Entry<T>): Boolean {
            val index = index(x, y, z, shift)
            return array[index].compareAndSet(null, subEntry)
        }

        @Suppress("NOTHING_TO_INLINE", "NAME_SHADOWING")
        private inline fun index(x: Int, y: Int, z: Int, shift: Int): Int {
            var x = x shr shift
            var y = y shr shift
            var z = z shr shift
            x = x and bitMask
            y = y and bitMask
            z = z and bitMask
            return ((x and bitMask) shl doubleBits) or ((y and bitMask) shl bits) or (z and bitMask)
        }
    }

    private class LeafEntry<T>(
        val x: Int,
        val y: Int,
        val z: Int
    ) : Entry<T> {
        private val valueRef = atomic<T?>(null)
        override val value: T
            get() = valueRef.value!!
        override val depth: Int
            get() = throw UnsupportedOperationException("${this::class.simpleName} does not support this method")
        override val initialShift: Int
            get() = throw UnsupportedOperationException("${this::class.simpleName} does not support this method")

        override fun getSubEntry(x: Int, y: Int, z: Int, shift: Int): Entry<T>? = null

        override fun testKey(x: Int, y: Int, z: Int): Boolean =
            this.x == x && this.y == y && this.z == z

        override fun remove(): T? = valueRef.getAndSet(null)

        override fun remove(value: T): Boolean = valueRef.compareAndSet(value, null)

        override fun put(value: T): T? = valueRef.getAndSet(value)

        override fun getOrPut(value: () -> T): T {
            while (true) {
                val old = valueRef.value
                if (old != null) {
                    return old
                }
                val newValue = value()
                if (valueRef.compareAndSet(null, newValue)) {
                    return newValue
                }
            }
        }

        override fun toString(): String = "{$x, $y, $z}"
    }
}
