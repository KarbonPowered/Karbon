package com.karbonpowered.engine.scheduler

/**
 * Represents the various tick stages.
 *
 * The exact bit fields used are subject to change
 */
object TickStage {
    /**
     * All tasks submitted to the main thread are executed during [TICK_START].
     *
     * This stage is single threaded
     */
    const val TICK_START = 1

    /**
     * This is the final stage before entering the pre-snapshot stage.
     *
     * This is for minor changes prior to the snapshot process.
     */
    const val FINALIZE = 1 shl 8

    /**
     * This stage occurs before the snapshot stage.
     *
     * This is a MONITOR ONLY stage, no changes should be made during the stage.
     */
    const val PRE_SNAPSHOT = 1 shl 9

    /**
     * This is the snapshot copy stage.
     *
     * All snapshots are updated to the equal to the live value.
     */
    const val SNAPSHOT = 1 shl 10

    var stage: Int = TICK_START

    /**
     * Checks if the current stages is one of the valid allowed stages, but does not throw an exception.
     * @param Params the OR of all the allowed stages
     */
    fun testStage(allowedStages: Int): Boolean = stage and allowedStages != 0

    fun getStage(num: Int): String = when (num) {
        TICK_START -> "TICK_START"
        FINALIZE -> "FINALIZE"
        PRE_SNAPSHOT -> "PRE_SNAPSHOT"
        SNAPSHOT -> "SNAPSHOT"
        else -> "UNKNOWN"
    }
}