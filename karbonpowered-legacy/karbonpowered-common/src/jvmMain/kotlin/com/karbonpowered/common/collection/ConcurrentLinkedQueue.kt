package com.karbonpowered.common.collection

import java.util.concurrent.ConcurrentLinkedQueue

actual class ConcurrentLinkedQueue<E> : AbstractQueue<E>() {
    val handle = ConcurrentLinkedQueue<E>()
    override val size: Int
        get() = handle.size

    override fun iterator(): MutableIterator<E> = handle.iterator()
    override fun offer(element: E): Boolean = handle.offer(element)
    override fun pool(): E? = handle.poll()
    override fun peek(): E? = handle.peek()
    override fun remove(element: E): Boolean = handle.remove(element)
    override fun removeAll(elements: Collection<E>): Boolean = handle.removeAll(elements)
    override fun retainAll(elements: Collection<E>): Boolean = handle.retainAll(elements)
}