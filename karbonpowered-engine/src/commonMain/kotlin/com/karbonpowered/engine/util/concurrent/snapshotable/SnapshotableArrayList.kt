package com.karbonpowered.engine.util.concurrent.snapshotable

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class SnapshotableArrayList<T>(
        val snapshotManager: SnapshotManager
) : Snapshotable, List<T> {
    init {
        snapshotManager.add(this)
    }

    private val liveLock = reentrantLock()
    private val _dirty = atomic(false)
    private val _live = arrayListOf<T>()
    private val _snapshot = arrayListOf<T>()
    val live: List<T> get() = _live
    val snapshot: List<T> get() = _snapshot
    var isDirty: Boolean
        get() = _dirty.value
        set(value) {
            _dirty.getAndSet(value)
        }

    override val size: Int
        get() = _snapshot.size

    fun add(element: T): Boolean {
        val success = liveLock.withLock {
            _live.add(element)
        }
        if (success) {
            _dirty.compareAndSet(false, true)
        }
        return success
    }

    fun addAll(elements: Collection<T>) {
        elements.forEach {
            val success = liveLock.withLock {
                _live.add(it)
            }
            if (success) {
                _dirty.compareAndSet(false, true)
            }
        }
    }

    fun remove(element: T): Boolean {
        val success = liveLock.withLock {
            _live.remove(element)
        }
        if (success) {
            _dirty.compareAndSet(false, true)
        }
        return success
    }

    fun removeAll(elements: Collection<T>) {
        elements.forEach {
            val success = liveLock.withLock {
                _live.remove(it)
            }
            if (success) {
                _dirty.compareAndSet(false, true)
            }
        }
    }

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

    override fun copySnapshot() {
        if (_dirty.value) {
            _snapshot.clear()
            liveLock.withLock {
                _snapshot.addAll(_live)
            }
            _dirty.getAndSet(false)
        }
    }
}