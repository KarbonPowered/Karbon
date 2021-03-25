package com.karbonpowered.api.tick

interface Tickable {
    fun onTick(dt: Float)

    fun canTick(): Boolean = true

    fun tick(dt: Float) {
        if(canTick()) {
            onTick(dt)
        }
    }
}