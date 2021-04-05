package com.karbonpowered.api.world.weather

interface WeatherUniverse {
    val weather: Weather

    interface Mutable : WeatherUniverse {
        override var weather: Weather

        fun setWeather(type: WeatherType, ticks: Int)
    }
}