package com.karbonpowered.engine.util

import com.karbonpowered.math.vector.IntVector3

class CuboidIntBuffer(
    base: IntVector3,
    size: IntVector3,
    val data: IntArray = IntArray(size.x * size.y * size.z)
) : CuboidBuffer(base, size) {
    operator fun set(x: Int, y: Int, z: Int, value: Int) {
        val index = checkedIndex(x, y, z)
        data[index] = value
    }

    operator fun get(x: Int, y: Int, z: Int): Int {
        val index = checkedIndex(x, y, z)
        return data[index]
    }

    fun fill(value: Int) {
        data.fill(value)
    }

    fun fillHorizontalLayer(y: Int, height: Int, value: Int) {
        val fromIndex = checkedIndex(base.x, y, base.z)
        val toIndex = checkedIndex(top.x - 1, y + height - 1, top.z - 1) + 1
        data.fill(value, fromIndex, toIndex)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun checkedIndex(x: Int, y: Int, z: Int): Int {
        val index = index(x, y, z)
        check(index >= 0) { "Coordinate ($x, $y, $z) is outside the buffer" }
        return index
    }

    fun copy() = CuboidIntBuffer(base.copy(), size.copy(), data.copyOf())
}
