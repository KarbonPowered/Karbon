package com.karbonpowered.api

object Karbon {
    lateinit var engine: Engine

    val game: Game get() = engine.game
}