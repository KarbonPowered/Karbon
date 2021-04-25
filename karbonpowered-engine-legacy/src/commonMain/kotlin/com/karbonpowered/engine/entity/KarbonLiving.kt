package com.karbonpowered.engine.entity

import com.karbonpowered.api.entity.living.Living

abstract class KarbonLiving<E : Living<E>> : Living<E>, KarbonEntity<E>() {
    override var health: Double = 20.0
}