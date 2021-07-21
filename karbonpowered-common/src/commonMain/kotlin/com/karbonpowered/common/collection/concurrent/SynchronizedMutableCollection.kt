package com.karbonpowered.common.collection.concurrent

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

open class SynchronizedMutableCollection<E>(
    val collection: MutableCollection<E>,
    val lock: ReentrantLock = reentrantLock()
) : MutableCollection<E> {
    override val size: Int
        get() = lock.withLock {
            collection.size
        }

    override fun contains(element: E): Boolean = lock.withLock { collection.contains(element) }

    override fun containsAll(elements: Collection<E>): Boolean = lock.withLock { collection.containsAll(elements) }

    override fun isEmpty(): Boolean = lock.withLock { collection.isEmpty() }

    override fun add(element: E): Boolean = lock.withLock { collection.add(element) }

    override fun addAll(elements: Collection<E>): Boolean = lock.withLock { collection.addAll(elements) }

    override fun clear() = lock.withLock { collection.clear() }

    override fun iterator(): MutableIterator<E> = collection.iterator()

    override fun remove(element: E): Boolean = lock.withLock { collection.remove(element) }

    override fun removeAll(elements: Collection<E>): Boolean = lock.withLock { collection.removeAll(elements) }

    override fun retainAll(elements: Collection<E>): Boolean = lock.withLock { collection.retainAll(elements) }
}

fun <E> MutableCollection<E>.synchronized(lock: ReentrantLock = reentrantLock()) =
    SynchronizedMutableCollection(this, lock)