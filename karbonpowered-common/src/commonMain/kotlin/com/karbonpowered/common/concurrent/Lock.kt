package com.karbonpowered.common.concurrent

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock

fun <T> Pair<ReentrantLock, ReentrantLock>.withLock(block: () -> T): T = dualLock(first, second, block)
fun <T> dualLock(first: ReentrantLock, second: ReentrantLock, block: () -> T): T {
    if (first == second) {
        return first.withLock(block)
    }

    while (true) {
        first.lock()
        if (second.tryLock()) {
            break
        }
        first.unlock()
        second.lock()
        if (first.tryLock()) {
            break
        }
        second.unlock()
    }

    try {
        return block()
    } finally {
        first.unlock()
        second.unlock()
    }
}

