package com.karbonpowered.engine.util.collection.map.palette.backing

import com.karbonpowered.engine.util.collection.map.palette.exception.PaletteFullException
import kotlinx.atomicfu.atomic

class AtomicShortIntUniformBackingArray(
    size: Int,
    previous: AbstractAtomicShortIntBackingArray? = null,
    initial: Int = previous?.let { it[0] } ?: 0
) : AbstractAtomicShortIntBackingArray(size) {
    constructor(previous: AbstractAtomicShortIntBackingArray) : this(previous.size, previous)

    val store = atomic(initial)

    init {
        copyFromPrevious(previous)
    }

    override val width: Int = 0
    override val palette: IntArray
        get() = intArrayOf(store.value)
    override val backingArray: IntArray = intArrayOf()
    override val paletteSize: Int = 1
    override val paletteUsage: Int = 1
    override val isPaletteMaxSize: Boolean = false

    override fun get(index: Int): Int = store.value

    override fun set(index: Int, value: Int): Int {
        if (!store.compareAndSet(value, value)) {
            throw PaletteFullException()
        }
        return value
    }

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        return if (store.value != expect) {
            false
        } else {
            if (expect != update) {
                throw PaletteFullException()
            }
            store.compareAndSet(expect, update)
        }
    }
}
