package com.karbonpowered.common.collection.concurrent

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class SynchronizedMutableSet<E>(
    val set: MutableSet<E>,
    lock: ReentrantLock = reentrantLock()
) : SynchronizedMutableCollection<E>(set, lock) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return lock.withLock {
            set == other
        }
    }

    override fun hashCode(): Int = lock.withLock { set.hashCode() }
}

fun <E> MutableSet<E>.synchronized(lock: ReentrantLock = reentrantLock()) =
    SynchronizedMutableSet(this, lock)