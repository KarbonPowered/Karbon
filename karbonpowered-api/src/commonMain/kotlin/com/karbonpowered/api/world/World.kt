package com.karbonpowered.api.world

import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.api.world.volume.RegionVolume
import com.karbonpowered.math.vector.IntVector3

interface World<W:World<W,L>, L:Location<W,L>> : RegionVolume