package com.karbonpowered.engine.util.concurrent

abstract class AtomicShortIntBackingArray(
    val length: Int
) {
    abstract operator fun get(index: Int): Int

    abstract operator fun set(index: Int, value: Int): Int

    abstract fun compareAndSet(index: Int, expect: Int, update: Int): Boolean

    fun getUnique(inUseSet: MutableSet<Int> = mutableSetOf()): Int {
        inUseSet.clear()
        var unique = 0
        repeat(length) {
            if (inUseSet.add(get(it))) {
                unique++
            }
        }
        return unique
    }

    protected fun copyFromPrevious(previous: AtomicShortIntBackingArray?) {
        if (previous != null) {
            repeat(length) {
                set(it, previous[it])
            }
        } else {
            set(0, 0)
        }
    }
}