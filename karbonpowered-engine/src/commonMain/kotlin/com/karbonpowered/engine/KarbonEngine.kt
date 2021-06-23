package com.karbonpowered.engine

import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.util.Log
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@OptIn(ExperimentalTime::class)
abstract class KarbonEngine : AsyncManager {
    lateinit var startTime: TimeMark

    open fun start() {
        startTime = TimeSource.Monotonic.markNow()
        info("Starting Karbon...")
        info("This software is currently in pre-alpha stage")
        info("Some components may have bugs or not work at all.")
        info("Please report any issues to:")
        info("https://github.com/KarbonPowered/Karbon/issues")
    }

    fun info(info: String) = Log.info("Karbon", info)

}