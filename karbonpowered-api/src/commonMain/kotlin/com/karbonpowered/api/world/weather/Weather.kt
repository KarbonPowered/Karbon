package com.karbonpowered.api.world.weather

interface Weather {
    val type: WeatherType

    val remainingTicks: Int
    val runningTicks: Int
}