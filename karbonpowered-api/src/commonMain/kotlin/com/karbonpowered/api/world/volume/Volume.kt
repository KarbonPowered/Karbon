package com.karbonpowered.api.world.volume

import com.karbonpowered.math.vector.IntVector3

interface Volume {
    val blockMin: IntVector3
    val blockMax: IntVector3
    val blockSize: IntVector3

    /**
     * Returns true if the block volume contains a block at the specified
     * position. This is defined as `[blockMin] <= [position] <= [blockMax]`
     *
     * @param position The position to check
     * @return Whether or not the position has a block in this volume
     */
    fun containsBlock(position: IntVector3): Boolean = containsBlock(position.x, position.y, position.z)

    /**
     * Returns true if the block volume contains a block at the specified
     * position. This is defined as `[blockMin] <= ([x], [y], [z]) <= [blockMax]`
     *
     * @param x The X coordinate to check
     * @param y The Y coordinate to check
     * @param z The Z coordinate to check
     * @return Whether or not the position has a block in this volume
     */
    fun containsBlock(x: Int, y: Int, z: Int): Boolean

    fun isAreaAvailable(position: IntVector3): Boolean {
        return this.isAreaAvailable(position.x, position.y, position.z)
    }

    fun isAreaAvailable(x: Int, y: Int, z: Int): Boolean
}

interface MutableVolume : Volume