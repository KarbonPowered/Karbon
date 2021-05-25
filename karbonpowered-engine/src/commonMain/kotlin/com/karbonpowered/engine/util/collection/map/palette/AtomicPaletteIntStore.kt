package com.karbonpowered.engine.util.collection.map.palette

import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic

class AtomicPaletteIntStore(
    val shift: Int,
    storeState: Boolean,
    dirtySize: Int = 10
) {
    private val doubleShift = shift shl 1
    private val dirtyX = ByteArray(dirtySize)
    private val dirtyY = ByteArray(dirtySize)
    private val dirtyZ = ByteArray(dirtySize)
    private val oldState = if (storeState) IntArray(dirtySize) else null
    private val newState = if (storeState) IntArray(dirtySize) else null
    private val maxX = atomic(0)
    private val maxY = atomic(0)
    private val maxZ = atomic(0)
    private val minX = atomic(0)
    private val minY = atomic(0)
    private val minZ = atomic(0)
    private val dirtyBlocksCounter = atomic(0)

    val side = 1 shl shift
    val size = side * side * side
    val store = AtomicShortIntArray(size)
    val dirtyBlocks get() = dirtyBlocksCounter.value
    val isDirty = dirtyBlocks > 0
    val isDirtyOverflow = dirtyBlocks >= dirtyX.size

    constructor(
        shift: Int,
        storeState: Boolean,
        dirtySize: Int,
        data: IntArray,
        compress: Boolean = true
    ) : this(shift, storeState, dirtySize) {
        if (compress) {
            store.set(data)
        } else {
            store.uncompressedSet(data)
        }
    }

    constructor(
        shift: Int,
        storeState: Boolean,
        dirtySize: Int,
        palette: IntArray,
        blockArrayWidth: Int,
        variableWidthArray: IntArray
    ) : this(shift, storeState, dirtySize) {
        store.set(palette, blockArrayWidth, variableWidthArray)
    }

    operator fun get(x: Int, y: Int, z: Int) = store[index(x, y, z)]

    operator fun set(x: Int, y: Int, z: Int, value: Int) = getAndSet(x, y, z, value)

    fun getAndSet(x: Int, y: Int, z: Int, value: Int): Int {
        var oldValue = 0
        try {
            oldValue = store.set(index(x, y, z), value)
            return oldValue
        } finally {
            markDirty(x, y, z, oldValue, value)
        }
    }

    fun compareAndSet(x: Int, y: Int, z: Int, expect: Int, update: Int): Boolean {
        val success = store.compareAndSet(index(x, y, z), expect, update)
        if (success && expect != update) {
            markDirty(x, y, z, expect, update)
        }
        return success
    }

    fun touch(x: Int, y: Int, z: Int): Int {
        val state = get(x, y, z)
        markDirty(x, y, z, state, state)
        return state
    }

    fun resetDirtyArrays(): Boolean {
        minX.value = 0
        minY.value = 0
        minZ.value = 0
        maxX.value = 0
        maxY.value = 0
        maxZ.value = 0
        return dirtyBlocksCounter.getAndSet(0) > 0
    }

    private fun markDirty(x: Int, y: Int, z: Int, oldValue: Int, value: Int) {
        minX.setMin(x)
        minY.setMin(y)
        minZ.setMin(z)

        maxX.setMax(x)
        maxY.setMax(y)
        maxZ.setMax(z)

        val index = incrementDirtyIndex()
        if (index < dirtyX.size) {
            dirtyX[index] = x.toByte()
            dirtyY[index] = y.toByte()
            dirtyZ[index] = z.toByte()
            if (oldState != null && newState != null) {
                oldState[index] = oldValue
                newState[index] = value
            }
        }
    }

    fun incrementDirtyIndex(): Int {
        var index = -1
        var success = false
        while (!success) {
            index = dirtyBlocksCounter.value
            if (index > dirtyX.size) {
                break
            }
            val next = index + 1
            success = dirtyBlocksCounter.compareAndSet(index, next)
        }
        return index
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun index(x: Int, y: Int, z: Int): Int =
        (y shl doubleShift) + (z shl shift) + x

    private fun AtomicInt.setMin(i: Int) {
        while (true) {
            val old = value
            if (old <= i) break
            if (compareAndSet(old, i)) {
                return
            }
        }
    }

    private fun AtomicInt.setMax(i: Int) {
        while (true) {
            val old = value
            if (old >= i) break
            if (compareAndSet(old, i)) {
                return
            }
        }
    }
}
