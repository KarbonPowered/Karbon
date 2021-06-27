package com.karbonpowered.engine.util.tick

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface Tickable {
    suspend fun tick(duration: Duration)
}