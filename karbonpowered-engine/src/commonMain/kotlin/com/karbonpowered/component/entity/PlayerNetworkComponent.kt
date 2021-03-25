package com.karbonpowered.component.entity

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.math.Transform

abstract class PlayerNetworkComponent : NetworkComponent() {
    private var sync: Boolean = false
    override fun onAttached() {
        check(owner is Player) { "PlayerNetworkComponent may only be attach to player" }
        super.onAttached()
    }

    abstract fun sendPositionUpdates(live: Transform)

    fun forceSync(){
        sync=true
    }
}