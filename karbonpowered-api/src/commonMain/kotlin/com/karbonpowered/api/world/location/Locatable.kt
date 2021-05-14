package com.karbonpowered.api.world.location

import com.karbonpowered.api.world.World
import com.karbonpowered.api.world.server.ServerLocation
import com.karbonpowered.math.vector.IntVector3

interface Locatable {
    val world: World<*, *>
    val location: Location<*, *>

    val serverLocation: ServerLocation
        get() = location as? ServerLocation
            ?: throw RuntimeException("Attempt made to query for a server sided location on the client!")

    val blockPosition: IntVector3
        get() = location.blockPosition
}