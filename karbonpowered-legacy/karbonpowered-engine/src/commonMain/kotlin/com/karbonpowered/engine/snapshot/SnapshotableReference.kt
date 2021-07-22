package com.karbonpowered.engine.snapshot

import kotlinx.atomicfu.atomic

/**
 * A snapshotable object that supports basic class types.
 *
 * This class should be used for immutable types that are updated by replacing with a new immutable object
 *
 * @param T the underlying type
 */
class SnapshotableReference<T>(
    manager: SnapshotManager,
    initial: T
) : Snapshotable {
    private val next = atomic(initial)
    private var snapshot = initial

    var value
        get() = snapshot
        set(value) {
            next.value = value
        }

    val live get() = next.value

    init {
        manager.add(this)
    }

    fun compareAndSet(expect: T, update: T): Boolean = next.compareAndSet(expect, update)

    fun isDirty(): Boolean = snapshot != next.value

    override fun copySnapshot() {
        snapshot = next.value
    }
}