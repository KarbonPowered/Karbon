package com.karbonpowered.api

import com.karbonpowered.api.event.EventManager

object Karbon {
    lateinit var engine: Engine

    val game: Game get() = engine.game
    val eventManager: EventManager get() = engine.game.eventManager
}