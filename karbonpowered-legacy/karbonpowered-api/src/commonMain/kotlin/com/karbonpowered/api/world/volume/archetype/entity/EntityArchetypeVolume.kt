package com.karbonpowered.api.world.volume.archetype.entity

import com.karbonpowered.api.world.volume.MutableVolume
import com.karbonpowered.api.world.volume.Volume

interface EntityArchetypeVolume : Volume {

    interface Sequence<B : Sequence<B>> : EntityArchetypeVolume

    interface Mutable<M : Mutable<M>> : Sequence<M>, MutableVolume
}