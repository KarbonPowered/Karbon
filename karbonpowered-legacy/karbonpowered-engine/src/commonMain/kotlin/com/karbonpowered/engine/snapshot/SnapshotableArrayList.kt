package com.karbonpowered.engine.snapshot

import com.karbonpowered.common.collection.ConcurrentLinkedQueue
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class SnapshotableArrayList<T>(
    manager: SnapshotManager,
    initial: List<T>? = null
) : Snapshotable, List<T> {
    init {
        manager.add(this)
    }

    private val dirty = ConcurrentLinkedQueue<T>()
    private val lock = reentrantLock()

    private val _snapshot = initial?.let { ArrayList(it) } ?: ArrayList()
    private val _live = ArrayList(_snapshot)

    val live: List<T> get() = _live.toList()
    val snapshot: List<T> get() = _snapshot.toList()

    override val size: Int get() = _snapshot.size
    override fun contains(element: T): Boolean = _snapshot.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = _snapshot.containsAll(elements)
    override fun get(index: Int): T = _snapshot[index]
    override fun indexOf(element: T): Int = _snapshot.indexOf(element)
    override fun isEmpty(): Boolean = _snapshot.isEmpty()
    override fun iterator(): Iterator<T> = _snapshot.iterator()
    override fun lastIndexOf(element: T): Int = _snapshot.lastIndexOf(element)
    override fun listIterator(): ListIterator<T> = _snapshot.listIterator()
    override fun listIterator(index: Int): ListIterator<T> = _snapshot.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<T> = _snapshot.subList(fromIndex, toIndex)

    fun add(element: T): Boolean {
        val success = lock.withLock { _live.add(element) }
        if (success) {
            dirty.add(element)
        }
        return success
    }

    fun addAll(values: Collection<T>) {
        lock.withLock {
            values.forEach { element ->
                val success = _live.add(element)
                if (success) {
                    dirty.add(element)
                }
            }
        }
    }

    fun remove(element: T): Boolean {
        val success = _live.remove(element)

        if (success) {
            dirty.add(element)
        }

        return success
    }

    override fun copySnapshot() {
        if (dirty.isNotEmpty()) {
            lock.withLock {
                _snapshot.clear()
                _snapshot.addAll(_live)
            }
            dirty.clear()
        }
    }
}