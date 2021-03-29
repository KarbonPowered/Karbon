package com.karbonpowered.engine.util.concurrent.snapshotable

interface Snapshotable<T> {
    val snapshotManager: SnapshotManager

    val live: T
    val snapshot: T
    var isDirty: Boolean

    fun copySnapshot()
}