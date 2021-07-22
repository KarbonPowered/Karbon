package com.karbonpowered.common.collection

interface Queue<E> : MutableCollection<E> {
    override fun add(element: E): Boolean

    fun offer(element: E): Boolean

    fun remove(): E

    fun pool(): E?

    fun element(): E

    fun peek(): E?
}