package com.karbonpowered.engine.util.collection.map.palette

import com.karbonpowered.engine.util.collection.map.palette.exception.PalleteFullException
import kotlinx.atomicfu.atomic

class AtomicShortIntUniformBackingArray(
    size: Int,
    previous: AtomicShortIntBackingArray? = null,
    initial: Int = previous?.let { it[0] } ?: 0
) : AtomicShortIntBackingArray(size) {
    constructor(previous: AtomicShortIntBackingArray) : this(previous.size, previous)

    val store = atomic(initial)

    init {
        copyFromPrevious(previous)
    }

    override val width: Int = 0
    override val palette: IntArray
        get() = intArrayOf(store.value)
    override val backingArray: IntArray
        get() = intArrayOf()

    override fun get(index: Int): Int = store.value

    override fun set(index: Int, value: Int): Int {
        if (!store.compareAndSet(value, value)) {
            throw PalleteFullException()
        }
        return value
    }

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        return if (store.value != expect) {
            false
        } else {
            if (expect != update) {
                throw PalleteFullException()
            }
            store.compareAndSet(expect, update)
        }
    }
}
