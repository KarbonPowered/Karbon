package com.karbonpowered.engine.entity

import com.karbonpowered.api.entity.living.Humanoid

abstract class KarbonHumanoid<E : Humanoid<E>> : Humanoid<E>, KarbonLiving<E>() {
}