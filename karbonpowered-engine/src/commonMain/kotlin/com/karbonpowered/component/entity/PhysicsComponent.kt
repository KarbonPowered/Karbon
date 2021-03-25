package com.karbonpowered.component.entity

import com.karbonpowered.math.Transform

abstract class PhysicsComponent : EntityComponent() {
    abstract val transform: Transform
}