package com.karbonpowered.engine.scheduler

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface TickManager {
    fun startTickRun(duration: Duration) {}
}