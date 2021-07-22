package com.karbonpowered.common.concurrent

import com.karbonpowered.common.Named
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> Pair<ReentrantLock, ReentrantLock>.withLock(block: () -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return dualLock(first, second, block)
}

@OptIn(ExperimentalContracts::class)
fun <T> dualLock(first: ReentrantLock, second: ReentrantLock, block: () -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

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

class NamedReentrantLock(override val name: String) : Named {
    val lock = reentrantLock()

    override fun toString(): String = "NamedReentrantLock($name)"

    inline fun <T> withLock(block: () -> T) = lock.withLock(block)
}

