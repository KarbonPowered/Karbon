package com.karbonpowered.engine.world.cuboid

import com.karbonpowered.engine.world.discrete.Position
import com.karbonpowered.math.vector.FloatVector3

open class Cuboid(
    val base: Position,
    val size: FloatVector3
) {
    val x = base.x / size.x
    val y = base.y / size.y
    val z = base.z / size.z
    val max = base + size

    val world get() = base.world

    operator fun contains(vector: FloatVector3): Boolean {
        return vector.x in base.x..max.x && vector.y in base.y..max.y && vector.z in base.z..max.z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cuboid

        if (base != other.base) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = base.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }

    override fun toString(): String = "Cuboid([${size.x}, ${size.y},${size.z}], [$x,$y,$z])"
}