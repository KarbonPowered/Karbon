package com.karbonpowered.event

import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class SimpleEventManager : EventManager {
    private val lock = reentrantLock()
    private val listeners = mutableSetOf<EventListener>()

    @OptIn(ExperimentalTime::class)
    override fun <T : Event> callEvent(event: T): T {
        listeners.forEach { listener ->
            try {
                val duration = measureTime {
                    event.call(listener)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return event
    }

    override fun registerListener(listener: EventListener) {
        lock.withLock {
            listeners.add(listener)
        }
    }
}