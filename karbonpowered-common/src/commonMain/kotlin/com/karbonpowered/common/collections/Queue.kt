package com.karbonpowered.common.collections

interface Queue<E> : Collection<E> {
    fun add(element: E): Boolean

    fun offer(element: E): Boolean

    fun remove(): E

    fun pool(): E?

    fun element(): E

    fun peek(): E?
}