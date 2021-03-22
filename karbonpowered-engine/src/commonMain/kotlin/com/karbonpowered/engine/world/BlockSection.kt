package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.VariableValueArray

class BlockSection(
        var data: VariableValueArray,
        var palette: List<Int>?
) {
    private var count: Int = 0

    fun getType(x: Int, y: Int, z: Int): Int = data[index(x, y, z)].let { palette?.get(it) ?: it }

    fun setType(x: Int, y: Int, z: Int, value: Int) {
        val palette = palette
        val oldType = getType(x, y, z)
        if (oldType != 0) {
            count--
        }
        if (value != 0) {
            count++
        }
        val encoded: Int = if (palette == null) {
            value
        } else {
            val paletteSize = palette.size
            if (paletteSize > data.valueMask) {
                if (data.bitsPerValue == 8) {
                    data = data.increaseBitsPerValueTo(GLOBAL_PALETTE_BITS_PER_BLOCK)
                    repeat(SECTION_SIZE) {
                        val oldValue = data[it]
                        val newValue = palette[oldValue]
                        data[it] = newValue
                    }
                    this.palette = null
                } else {
                    data = data.increaseBitsPerValueTo(data.bitsPerValue + 1)
                }
                value
            } else {
                paletteSize
            }
        }
        data[index(x, y, z)] = encoded
    }

    fun recount(): Int {
        var count = 0
        val palette = palette
        repeat(SECTION_SIZE) {
            var type = data[it]
            if (palette != null) {
                type = palette[type]
            }
            if (type != 0) {
                count++
            }
        }
        this.count = count
        return count
    }

    fun index(x: Int, y: Int, z: Int): Int = ((y and 0xf) shl 8) or (z shl 4) or x

    fun isEmpty(): Boolean = count == 0

    fun copy(): BlockSection = BlockSection(data.copy(), palette?.let { ArrayList(it) })

    companion object {
        const val SECTION_WIDTH = 16
        const val SECTION_HEIGHT = 16
        const val SECTION_DEPTH = 16
        const val SECTION_SIZE = SECTION_WIDTH * SECTION_HEIGHT * SECTION_DEPTH
        const val GLOBAL_PALETTE_BITS_PER_BLOCK = 13
    }
}
