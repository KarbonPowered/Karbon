package com.karbonpowered.common.collection

abstract class AbstractQueue<E> : AbstractCollection<E>(), Queue<E> {
    override fun add(element: E): Boolean =
        if (offer(element)) true else throw IllegalStateException("Queue full")

    override fun remove(): E =
        pool() ?: throw NoSuchElementException()

    override fun element(): E =
        peek() ?: throw NoSuchElementException()

    override fun clear() {
        while (true) {
            pool() ?: break
        }
    }

    override fun addAll(elements: Collection<E>): Boolean {
        if (elements == this) throw IllegalArgumentException()
        var modified = false
        elements.forEach {
            if (add(it)) {
                modified = true
            }
        }
        return modified
    }
}