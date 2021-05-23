package com.karbonpowered.engine.util.collection.map.palette

import com.karbonpowered.engine.util.collection.map.palette.backing.AbstractAtomicShortIntBackingArray
import com.karbonpowered.engine.util.collection.map.palette.backing.AtomicShortIntDirectBackingArray
import com.karbonpowered.engine.util.collection.map.palette.backing.AtomicShortIntPaletteBackingArray
import com.karbonpowered.engine.util.collection.map.palette.backing.AtomicShortIntUniformBackingArray
import com.karbonpowered.engine.util.collection.map.palette.exception.PaletteFullException
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

/**
 * An integer array that has a short index.
 * The array is atomic and is backed by a palette based lookup system.
 */
class AtomicShortIntArray(
    val size: Int
) {
    /**
     * A reference to the store.
     * When the palette fills, or when the store is compressed a new store is created.
     */
    private val store: AtomicRef<AbstractAtomicShortIntBackingArray> = atomic(AtomicShortIntUniformBackingArray(size))

    /**
     * Locks
     *
     * When copying to a new store instance, and updating to new the store reference,
     * all updates must be stopped.
     *
     * When making changes to the data stored in an array instance, multiple threads
     * can access the array concurrently.
     *
     * Reads to the array are atomic and do not require any locking.
     */
    val resizeLock = reentrantLock()
    val updateLock = reentrantLock()

    /**
     * Width of the internal array, in bits
     */
    val width: Int get() = store.value.width

    /**
     * Size of the internal palette
     */
    val paletteSize: Int get() = store.value.paletteSize

    /**
     * Number of palette entries in use
     */
    val paletteUsage: Int get() = store.value.paletteUsage

    /**
     * Number of unique entries in the array
     */
    val unique: Int
        get() {
            val inUse = HashSet<Int>()
            var count = 0
            repeat(size) {
                if (inUse.add(get(it))) {
                    count++
                }
            }
            return count
        }

    /**
     * Gets an element from the array at a given index
     */
    operator fun get(index: Int): Int = store.value[index]

    /**
     * Sets an element to the given value
     *
     * @param index index
     * @param value new value
     * @return old value
     */
    operator fun set(index: Int, value: Int): Int {
        while (true) {
            try {
                updateLock.withLock {
                    return store.value.set(index, value)
                }
            } catch (e: PaletteFullException) {
                resizeLock.withLock {
                    try {
                        return store.value.set(index, value)
                    } catch (e: PaletteFullException) {
                        store.value = if (store.value.isPaletteMaxSize) {
                            AtomicShortIntDirectBackingArray(store.value)
                        } else {
                            AtomicShortIntPaletteBackingArray(store.value, expand = true)
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the array equal to the given array.
     * The array should be the same length as this array
     *
     * @param initial the array containing the new values
     */
    fun set(initial: IntArray) {
        require(initial.size == size) { "Array length mismatch, expected $size, got ${initial.size}" }
        val unique = initial.toSet().size
        val allowedPalette = AtomicShortIntPaletteBackingArray.allowedPalette(size)
        resizeLock.withLock {
            store.value = when {
                unique == 1 -> AtomicShortIntUniformBackingArray(size, initial = initial.first())
                unique > allowedPalette -> AtomicShortIntDirectBackingArray(initial)
                else -> AtomicShortIntPaletteBackingArray(size, unique, initial)
            }
        }
    }

    /**
     * Sets the array equal to the given palette based array.
     * The main array should be the same length as this array
     *
     * @param palette the palette, if the palette is of length 0, variableWidthBlockArray contains the data, in flat format
     * @param blockArrayWidth array with of each entry in the main array
     * @param variableWidthBlockArray the array containing the new values, packed into ints
     */
    fun set(palette: IntArray, blockArrayWidth: Int, variableWidthBlockArray: IntArray) = resizeLock.withLock {
        store.value = when {
            palette.isEmpty() -> AtomicShortIntDirectBackingArray(variableWidthBlockArray)
            palette.size == 1 -> AtomicShortIntUniformBackingArray(size, initial = palette.first())
            else -> AtomicShortIntPaletteBackingArray(size, palette, blockArrayWidth, variableWidthBlockArray)
        }
    }

    /**
     * Sets the array equal to the given array without automatically compressing the data.
     * The array should be the same length as this array
     */
    fun uncompressedSet(initial: IntArray) {
        require(initial.size == size) { "Array length mismatch, expected $size, got ${initial.size}" }
        resizeLock.withLock {
            store.value = AtomicShortIntDirectBackingArray(initial)
        }
    }

    /**
     * Sets the element at the given index,
     * but only if the previous value was the expected value.
     *
     * @param index index for set
     * @param expect expected value
     * @param update new value
     * @return `true` if success
     */
    fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        while (true) {
            try {
                updateLock.withLock {
                    return store.value.compareAndSet(index, expect, update)
                }
            } catch (e: PaletteFullException) {
                resizeLock.withLock {
                    store.value = if (store.value.isPaletteMaxSize) {
                        AtomicShortIntDirectBackingArray(store.value)
                    } else {
                        AtomicShortIntPaletteBackingArray(store.value, expand = true)
                    }
                }
            }
        }
    }

    /**
     * Attempts to compress the array
     */
    fun compress(inUseSet: MutableSet<Int> = mutableSetOf()) {
        resizeLock.withLock {
            val array = store.value
            if (array is AtomicShortIntUniformBackingArray) {
                return
            }
            val unique = array.unique(inUseSet)
            if (AtomicShortIntPaletteBackingArray.roundUpWith(unique - 1) >= array.width) {
                return
            }
            if (unique > AtomicShortIntPaletteBackingArray.allowedPalette(array.size)) {
                return
            }
            if (unique == 1) {
                store.value = AtomicShortIntUniformBackingArray(array)
            } else {
                store.value = AtomicShortIntPaletteBackingArray(array, compress = true, expand = false, unique)
            }
        }
    }
}
