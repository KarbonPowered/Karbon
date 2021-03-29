package com.karbonpowered.engine.scheduler

import com.karbonpowered.engine.dateNow
import com.karbonpowered.engine.util.concurrent.snapshotable.SnapshotManager
import com.karbonpowered.engine.util.concurrent.snapshotable.SnapshotableArrayList
import com.karbonpowered.logging.Logger
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.milliseconds

@OptIn(ExperimentalTime::class)
object KarbonScheduler {
    val PULSE_DURATION = 50.milliseconds
    val tickStartTime = atomic(TimeSource.Monotonic.markNow())
    val tickNumber = atomic(0L)
    val snapshotManager = SnapshotManager()
    private val tickManagers = SnapshotableArrayList<TickManager>(snapshotManager)

    val mainJob = GlobalScope.launch {
        var lastTick = TimeSource.Monotonic.markNow()
        var expectedDuration: Duration
        while (true) {
            val startTick = TimeSource.Monotonic.markNow()
            try {
                tick(lastTick.elapsedNow())
            } catch (e: Exception) {
                Logger.error("Error while ticking", e)
            }
            expectedDuration = startTick.elapsedNow()
            lastTick = TimeSource.Monotonic.markNow()
            delay(PULSE_DURATION-expectedDuration)
        }
    }

    private fun tick(duration: Duration) {
        tickManagers.copySnapshot()
    }
}
