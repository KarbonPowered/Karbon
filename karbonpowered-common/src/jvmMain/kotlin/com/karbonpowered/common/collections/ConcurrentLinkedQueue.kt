package com.karbonpowered.common.collections

import java.util.concurrent.ConcurrentLinkedQueue

actual class ConcurrentLinkedQueue<E> : AbstractQueue<E>() {
    val handle = ConcurrentLinkedQueue<E>()
    override val size: Int
        get() = handle.size

    override fun iterator(): Iterator<E> = handle.iterator()

    override fun offer(element: E): Boolean = handle.offer(element)

    override fun pool(): E? = handle.poll()

    override fun peek(): E? = handle.peek()
}