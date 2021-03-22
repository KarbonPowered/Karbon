package com.karbonpowered.engine.util

/**
 * An array of nibbles (4-bit values) stored efficiently as a byte array of half the size.
 *
 * The even indices are stored in the least significant nibble and the odd indices in the most
 * significant bits. For example, `[1 5 8 15]` is stored as `[0x51 0xf8]`.
 */
class NibbleArray(
        private val rawData: ByteArray
) {
    val size: Int get() = 2 * rawData.size
    val byteSize: Int get() = rawData.size

    operator fun get(index: Int): Byte {
        val value = rawData[index / 2].toInt()
        return (if (index % 2 == 0) value and 0x0f else value and 0xf0 shr 4).toByte()
    }

    operator fun set(index: Int, value: Byte) {
        val v = value.toInt() and 0xf
        val half = index / 2
        val previous = rawData[half].toInt()
        rawData[half] = (if (index % 2 == 0) (previous and 0xf0) or v else (previous and 0x0f) or (v shl 4)).toByte()
    }

    fun fill(value: Byte) {
        val v = value.toInt() and 0xf
        rawData.fill(((v shl 4) or v).toByte())
    }

    fun copy(): NibbleArray = NibbleArray(rawData.copyOf())
}