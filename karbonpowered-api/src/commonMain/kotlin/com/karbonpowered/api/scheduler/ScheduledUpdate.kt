package com.karbonpowered.api.scheduler

import com.karbonpowered.api.world.location.Locatable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface ScheduledUpdate<T> : Locatable {
    val target: T
    val delay: Duration


}