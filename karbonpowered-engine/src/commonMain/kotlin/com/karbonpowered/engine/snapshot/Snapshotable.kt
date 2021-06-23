package com.karbonpowered.engine.snapshot

interface Snapshotable {
    /**
     * Copies the next value to the snapshot value
     */
    fun copySnapshot()
}