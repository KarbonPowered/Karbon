package com.karbonpowered.api.world.volume.game

interface GenerationVolume : HeightAwareVolume {

    interface Mutable : GenerationVolume, MutableGameVolume
}