package com.karbonpowered.engine.entity

import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.BaseComponentHolder
import com.karbonpowered.engine.component.PhysicsComponent

open class KarbonEntity(
    val engine: KarbonEngine
) {
    val components = BaseComponentHolder(engine)

    init {
        components.add(PhysicsComponent())
    }
}