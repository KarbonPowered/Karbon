package com.karbonpowered.engine.util

class BitSize(
    val BITS: Int
) {
    val SIZE = 1 shl BITS
    val AREA = SIZE * SIZE
    val VOLUME = AREA * SIZE
    val HALF_SIZE = SIZE shr 1
    val HALF_AREA = AREA shr 1
    val HALF_VOLUME = VOLUME shr 1
    val DOUBLE_SIZE = SIZE shl 1
    val DOUBLE_AREA = AREA shl 1
    val DOUBLE_VOLUME = VOLUME shl 1
    val DOUBLE_BITS = BITS shl 1
    val MASK = SIZE - 1
}