package com.karbonpowered.engine.util.collection.map.palette

import kotlinx.atomicfu.AtomicIntArray

class AtomicShortIntDirectBackingArray(
    size: Int,
    val previous: AtomicShortIntBackingArray? = null
) : AtomicShortIntBackingArray(size) {
    val store = AtomicIntArray(size)


    constructor(previous: AtomicShortIntBackingArray) : this(previous.size, previous)

    init {
        copyFromPrevious(previous)
    }

    override val palette: IntArray
        get() = TODO("Not yet implemented")
    override val backingArray: IntArray
        get() = TODO("Not yet implemented")

    override fun get(index: Int): Int {
        TODO("Not yet implemented")
    }

    override fun set(index: Int, value: Int): Int {
        TODO("Not yet implemented")
    }

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        TODO("Not yet implemented")
    }
}
