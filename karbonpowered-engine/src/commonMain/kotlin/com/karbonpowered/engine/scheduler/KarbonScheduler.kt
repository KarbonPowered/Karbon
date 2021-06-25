package com.karbonpowered.engine.scheduler

import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.SnapshotableArrayList
import com.karbonpowered.engine.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


/**
 * The threshold before physics and dynamic updates are aborted
 */
private const val UPDATE_THRESHOLD = 100000

/**
 * The number of milliseconds between pulses.
 */
private const val PULSE_EVERY_MS = 1000L

@OptIn(ExperimentalTime::class)
class KarbonScheduler(
    val engine: KarbonEngine
) : TaskManager, CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    /**
     * A snapshot manager for local snapshot variables
     */
    private val snapshotManager = SnapshotManager()

    /**
     * A list of all AsyncManagers
     */
    private val asyncManagers = SnapshotableArrayList<AsyncManager>(snapshotManager)

    fun addAsyncManager(manager: AsyncManager): Boolean {
        engine.info("Add async manager: $manager")
        return asyncManagers.add(manager)
    }

    fun removeAsyncManager(manager: AsyncManager) = asyncManagers.remove(manager)

    fun runMainTask() = launch {
        while (true) {
            delay(PULSE_EVERY_MS)
            val tickDuration = measureTime {
                tick(1000)
            }
//            engine.info("Last tick duration: $tickDuration")
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
        managers.forEach { manager ->
            launch {
                measureTime {
                    manager.copySnapshotRun()
                }.also {
//                    engine.info("ticking: $manager ($it)")
                }
            }
        }
    }
}