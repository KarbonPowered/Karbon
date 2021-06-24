package com.karbonpowered.engine.scheduler

import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.SnapshotableArrayList
import com.karbonpowered.engine.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

object KarbonScheduler : TaskManager, CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    /**
     * The threshold before physics and dynamic updates are aborted
     */
    private const val UPDATE_THRESHOLD = 100000

    /**
     * The number of milliseconds between pulses.
     */
    const val PULSE_EVERY_MS = 50

    /**
     * A snapshot manager for local snapshot variables
     */
    private val snapshotManager = SnapshotManager()

    /**
     * A list of all AsyncManagers
     */
    private val asyncManagers = SnapshotableArrayList<AsyncManager>(snapshotManager)

    fun addAsyncManager(manager: AsyncManager) = asyncManagers.add(manager)

    fun removeAsyncManager(manager: AsyncManager) = asyncManagers.remove(manager)

    fun runMainTask() = launch {
        while (true) {
            delay(1000)
            tick(1000)
        }
    }

    private suspend fun tick(delta: Long) {
        asyncManagers.copySnapshot()
        val managers = asyncManagers.snapshot

        // TODO: Dynamic Updates
        // TODO: Physics
        // TODO: Lightning
        var totalUpdates = -1
        var lightUpdates = 0
        var dynamicUpdates = 0
        var physicsUpdates = 0

        if (totalUpdates >= UPDATE_THRESHOLD) {
            Log.warn(
                "Karbon",
                "Block updates per tick of $totalUpdates exceeded the threshold $UPDATE_THRESHOLD; $dynamicUpdates dynamic updates, $physicsUpdates physics updates, $lightUpdates lightning updates"
            )
        }

        finalizeTick(managers).join()
        preSnapshotTick(managers).join()
        copySnapshotTick(managers).join()
    }

    private fun finalizeTick(managers: Iterable<AsyncManager>) = launch {
        managers.forEach {
            launch {
                it.finalizeRun()
            }
        }
    }

    private fun preSnapshotTick(managers: Iterable<AsyncManager>) = launch {
        managers.forEach {
            launch {
                it.preSnapshotRun()
            }
        }
    }

    private fun copySnapshotTick(managers: Iterable<AsyncManager>) = launch {
        managers.forEach {
            launch {
                it.copySnapshotRun()
            }
        }
    }
}