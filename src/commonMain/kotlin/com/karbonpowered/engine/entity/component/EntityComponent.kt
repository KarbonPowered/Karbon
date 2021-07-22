package com.karbonpowered.engine.entity.component

import com.karbonpowered.component.Component
import com.karbonpowered.engine.entity.KarbonEntity

abstract class EntityComponent(
    val entity: KarbonEntity
) : Component {

}