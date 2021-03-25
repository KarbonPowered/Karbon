package com.karbonpowered.api.world

import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3
import kotlin.math.roundToInt

interface Location<W : World<W, L>, L : Location<W, L>> : DoubleVector3 {
    val world: W?

    val blockPosition: IntVector3
        get() = IntVector3(x, y, z)
    val chunkPosition: IntVector3
        get() = IntVector3(x.roundToInt() shl 4, y.roundToInt() shl 4, z.roundToInt() shl 4)

    val blockX: Int get() = blockPosition.x
    val blockY: Int get() = blockPosition.y
    val blockZ: Int get() = blockPosition.z
}