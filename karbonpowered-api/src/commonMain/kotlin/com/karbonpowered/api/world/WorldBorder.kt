package com.karbonpowered.api.world

import com.karbonpowered.math.vector.DoubleVector2
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface WorldBorder {
    val newDiameter: Double
    var diameter: Double
    val timeRemaining: Duration
    var center: DoubleVector2
    var warningTime: Duration
    var warningDistance: Double
    var damageThreshold: Double
    var damageAmount: Double

    fun setCenter(x: Double, y: Double)
    fun setDiameter(diameter: Double, duration: Duration)

    interface Builder : com.karbonpowered.common.builder.Builder<WorldBorder, Builder> {
        var diameter: Double
        var center: DoubleVector2
        var warningTime: Duration
        var warningDistance: Double
        var damageThreshold: Double
        var damageAmount: Double
    }
}