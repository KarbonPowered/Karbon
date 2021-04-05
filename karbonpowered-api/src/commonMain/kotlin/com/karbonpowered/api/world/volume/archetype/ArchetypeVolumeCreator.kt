package com.karbonpowered.api.world.volume.archetype

import com.karbonpowered.api.world.volume.Volume
import com.karbonpowered.math.vector.IntVector3

interface ArchetypeVolumeCreator : Volume {
    /**
     * Creates a new archetype volume from the specified section of this extent.
     * The archetype's volume will be shifted such that the position given in
     * the origin will be the origin of the volume.
     *
     * @param min The minimum point of the volume to copy
     * @param max The maximum point of the volume to copy
     * @param origin The eventual origin on the new archetype volume
     * @return The archetype volume
     */
    fun createArchetypeVolume(min: IntVector3, max: IntVector3, origin: IntVector3): ArchetypeVolume
}