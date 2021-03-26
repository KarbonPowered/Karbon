package com.karbonpowered.component.entity

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.math.vector.DoubleVector3

abstract class PlayerNetworkComponent : NetworkComponent() {
    private var sync: Boolean = false
    override fun onAttached() {
        check(owner is Player) { "PlayerNetworkComponent may only be attach to player" }
        super.onAttached()
    }

    abstract suspend fun sendPositionUpdates(position: DoubleVector3, rotation: DoubleVector3)

    fun forceSync(){
        sync=true
    }
}