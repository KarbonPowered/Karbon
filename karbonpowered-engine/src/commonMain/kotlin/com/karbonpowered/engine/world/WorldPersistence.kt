package com.karbonpowered.engine.world

import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.world.generator.WorldGenerator

object WorldPersistence {

    fun loadWorld(engine: KarbonEngine, name: String, generator: WorldGenerator): KarbonWorld {
        return KarbonWorld(engine, name, generator)
    }
}