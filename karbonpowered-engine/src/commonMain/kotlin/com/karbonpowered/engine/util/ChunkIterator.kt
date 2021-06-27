package com.karbonpowered.engine.util

import com.karbonpowered.math.vector.IntVector3

fun interface ChunkIterator {
    fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3>
}