package com.karbonpowered.engine.util.collection.map.palette

import kotlinx.atomicfu.AtomicIntArray

abstract class AtomicShortIntBackingArray(
    val size: Int
) {
    abstract val width: Int
    abstract val palette: IntArray
    abstract val backingArray: IntArray
    val unique: Int get() = getUnique()

    abstract operator fun get(index: Int): Int

    abstract operator fun set(index: Int, value: Int): Int

    abstract fun compareAndSet(index: Int, expect: Int, update: Int): Boolean

    fun getUnique(inUseSet: MutableSet<Int> = mutableSetOf()): Int {
        inUseSet.clear()
        var unique = 0
        repeat(size) {
            if (inUseSet.add(get(it))) {
                unique++
            }
        }
        return unique
    }

    protected fun copyFromPrevious(previous: AtomicShortIntBackingArray?) {
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
