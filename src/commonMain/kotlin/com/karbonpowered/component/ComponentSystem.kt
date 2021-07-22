@file:OptIn(ExperimentalTime::class)

package com.karbonpowered.component

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

interface ComponentSystem<H> : Iterable<H> {
    val holders: Collection<H>

    override fun iterator(): Iterator<H> = holders.iterator()

    fun offer(holder: H)

    fun process(holder: H, duration: Duration)
}