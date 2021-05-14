package com.karbonpowered.engine.util.concurrent

import kotlinx.atomicfu.atomic

class AtomicShortIntUniformBackingArray(
    length: Int,
    previous: AtomicShortIntBackingArray? = null,
    initial: Int = previous?.let { it[0] } ?: 0
) : AtomicShortIntBackingArray(length) {
    constructor(previous: AtomicShortIntBackingArray) : this(previous.length, previous)

    val store = atomic(initial)

    init {
        copyFromPrevious(previous)
    }

    override fun get(index: Int): Int = store.value

    override fun set(index: Int, value: Int): Int {
        store.compareAndSet(value, value)
        return value
    }

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        return if (store.value != expect) {
            false
        } else {
            store.compareAndSet(expect, update)
        }
    }
}