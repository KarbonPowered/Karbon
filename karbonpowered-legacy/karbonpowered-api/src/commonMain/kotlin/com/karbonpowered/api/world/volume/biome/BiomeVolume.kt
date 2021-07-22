package com.karbonpowered.api.world.volume.biome

import com.karbonpowered.api.world.biome.Biome
import com.karbonpowered.api.world.volume.MutableVolume
import com.karbonpowered.api.world.volume.Volume
import com.karbonpowered.api.world.volume.sequence.SequenceOptions
import com.karbonpowered.api.world.volume.sequence.VolumeSequence
import com.karbonpowered.math.vector.IntVector3

interface BiomeVolume : Volume {
    fun biome(position: IntVector3): Biome = biome(position.x, position.y, position.z)
    fun biome(x: Int, y: Int, z: Int): Biome

    interface Sequence<B : Sequence<B>> : BiomeVolume {
        fun biomeSequence(min: IntVector3, max: IntVector3, option: SequenceOptions): VolumeSequence<B, Biome>
    }

    interface Mutable<M : Mutable<M>> : Sequence<M>, MutableVolume {
        fun setBiome(position: IntVector3, biome: Biome): Boolean =
            setBiome(position.x, position.y, position.z, biome)

        fun setBiome(x: Int, y: Int, z: Int, biome: Biome): Boolean
    }
}