package com.karbonpowered.engine.util.concurrent.snapshotable

import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class SnapshotManager {
    private val lock = reentrantLock()
    private val managed = arrayListOf<Snapshotable>()

    fun add(snapshotable: Snapshotable) {
        lock.withLock {
            managed.add(snapshotable)
        }
    }

    fun copyAllSnapshots() {
        lock.withLock {
            managed.forEach {
                it.copySnapshot()
            }
        }
    }
}