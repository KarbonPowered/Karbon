package com.karbonpowered.engine.util.collection.map.palette.backing

import kotlinx.atomicfu.AtomicIntArray

class AtomicShortIntDirectBackingArray(
    size: Int,
    previous: AbstractAtomicShortIntBackingArray? = null
) : AbstractAtomicShortIntBackingArray(size) {
    init {
        copyFromPrevious(previous)
    }

    val store = AtomicIntArray(size)

    constructor(previous: AbstractAtomicShortIntBackingArray) : this(previous.size, previous)

    constructor(initial: IntArray) : this(initial.size) {
        initial.forEachIndexed { index, i ->
            store[index].value = i
        }
    }

    override val width: Int = AtomicShortIntPaletteBackingArray.roundUpWith(size - 1)
    override val palette: IntArray = NO_PALETTE
    override val backingArray: IntArray
        get() = toIntArray(store)
    override val isPaletteMaxSize: Boolean = true
    override val paletteSize: Int = size
    override val paletteUsage: Int = size

    override fun get(index: Int): Int = store[index].value

    override fun set(index: Int, value: Int): Int = store[index].getAndSet(value)

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean =
        store[index].compareAndSet(expect, update)

    companion object {
        private val NO_PALETTE = intArrayOf()
    }
}
