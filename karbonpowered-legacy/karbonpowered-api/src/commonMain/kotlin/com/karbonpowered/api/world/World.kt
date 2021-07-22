package com.karbonpowered.api.world

import com.karbonpowered.api.context.ContextSource
import com.karbonpowered.api.effect.Viewer
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.registry.ScopedRegistryHolder
import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.api.world.location.Location
import com.karbonpowered.api.world.location.LocationCreator
import com.karbonpowered.api.world.volume.ChunkVolume
import com.karbonpowered.api.world.volume.RegionVolume
import com.karbonpowered.api.world.volume.archetype.ArchetypeVolumeCreator
import com.karbonpowered.api.world.volume.block.PhysicsAwareMutableBlockVolume
import com.karbonpowered.api.world.weather.WeatherUniverse
import com.karbonpowered.audience.Audience
import com.karbonpowered.audience.ForwardingAudience

interface World<W : World<W, L>, L : Location<W, L>> :
    ForwardingAudience,
    ProtoWorld<W>,
    LocationCreator<W, L>,
    PhysicsAwareMutableBlockVolume<W>,
    ContextSource,
    Viewer,
    ArchetypeVolumeCreator,
    WeatherUniverse,
    RegionVolume,
    ChunkVolume,
    ScopedRegistryHolder {

    @Suppress("UNCHECKED_CAST")
    override val world: W
        get() = this as W

    val isLoaded: Boolean

    override val players: Collection<Player>
    val loadedChunks: Iterable<Chunk>

    override val audiences: Iterable<Audience>
        get() = players

}
