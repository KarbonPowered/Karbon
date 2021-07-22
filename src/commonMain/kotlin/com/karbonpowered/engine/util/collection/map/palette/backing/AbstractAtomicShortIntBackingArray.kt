package com.karbonpowered.engine.util.collection.map.palette.backing

import kotlinx.atomicfu.AtomicIntArray

abstract class AbstractAtomicShortIntBackingArray(
    val size: Int
) {
    abstract val isPaletteMaxSize: Boolean
    abstract val width: Int
    abstract val palette: IntArray
    abstract val backingArray: IntArray
    abstract val paletteSize: Int
    abstract val paletteUsage: Int
    val unique: Int get() = unique()

    abstract operator fun get(index: Int): Int

    abstract operator fun set(index: Int, value: Int): Int

    abstract fun compareAndSet(index: Int, expect: Int, update: Int): Boolean

    fun unique(inUseSet: MutableSet<Int> = mutableSetOf()): Int {
        inUseSet.clear()
        var unique = 0
        repeat(size) {
            if (inUseSet.add(get(it))) {
                unique++
            }
        }
        return unique
    }

    protected fun copyFromPrevious(previous: AbstractAtomicShortIntBackingArray?) {
        if (previous != null) {
            repeat(size) {
                set(it, previous[it])
            }
        } else {
            set(0, 0)
        }
    }

    protected fun toIntArray(array: AtomicIntArray, size: Int = array.size): IntArray {
        val packed = IntArray(size)
        repeat(size) {
            packed[it] = array[it].value
        }
        return packed
    }
}
