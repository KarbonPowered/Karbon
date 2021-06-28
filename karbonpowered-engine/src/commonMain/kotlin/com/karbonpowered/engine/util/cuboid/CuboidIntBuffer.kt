package com.karbonpowered.engine.util.cuboid

open class CuboidIntBuffer(
    baseX: Int,
    baseY: Int,
    baseZ: Int,
    sizeX: Int,
    sizeY: Int,
    sizeZ: Int,
    val data: IntArray = IntArray(sizeX * sizeY * sizeZ)
) : CuboidBuffer(baseX, baseY, baseZ, sizeX, sizeY, sizeZ) {
    open operator fun set(x: Int, y: Int, z: Int, value: Int) {
        val index = checkedIndex(x, y, z)
        data[index] = value
    }

    open operator fun get(x: Int, y: Int, z: Int): Int {
        val index = checkedIndex(x, y, z)
        return data[index]
    }

    open fun fill(value: Int) {
        data.fill(value)
    }

    open fun fillHorizontalLayer(value: Int, y: Int, height: Int = y) {
        val fromIndex = checkedIndex(baseX, y, baseZ)
        val toIndex = checkedIndex(topX - 1, y + height - 1, topZ - 1) + 1
        data.fill(value, fromIndex, toIndex)
    }

    override fun copyElement(thisIndex: Int, sourceIndex: Int, runLength: Int, source: CuboidBuffer) {
        require(source is CuboidIntBuffer)
        val end = thisIndex + runLength
        var currentSourceIndex = sourceIndex
        for (i in thisIndex until end) {
            data[i] = source.data[currentSourceIndex++]
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun checkedIndex(x: Int, y: Int, z: Int): Int {
        val index = index(x, y, z)
        check(index >= 0) { "Coordinate ($x, $y, $z) is outside the buffer" }
        return index
    }

    fun copy() = CuboidIntBuffer(baseX, baseY, baseZ, sizeX, sizeY, sizeZ, data.copyOf())
}
