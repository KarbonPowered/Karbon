package com.karbonpowered.api.tick

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface Tickable {
    suspend fun onTick(duration: Duration)

    fun canTick(): Boolean = true

    suspend fun tick(duration: Duration)
}