package com.karbonpowered.engine.snapshot

import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.collections.immutable.toImmutableList

class SnapshotManager : Iterable<Snapshotable> {
    private val lock = reentrantLock()
    private val entries = ArrayList<Snapshotable>()

    fun add(snapshotable: Snapshotable) = lock.withLock {
        entries.add(snapshotable)
    }

    fun copyAllSnapshots() = lock.withLock {
        entries.forEach {
            it.copySnapshot()
        }
    }

    override fun iterator(): Iterator<Snapshotable> = lock.withLock {
        entries.toImmutableList().iterator()
    }
}