package com.karbonpowered.math

val MULTIPLY_DE_BRUIJN_BIT_POSITION = intArrayOf(
    0,
    1,
    28,
    2,
    29,
    14,
    24,
    3,
    30,
    22,
    20,
    15,
    25,
    17,
    4,
    8,
    31,
    27,
    13,
    23,
    21,
    19,
    16,
    7,
    26,
    12,
    18,
    6,
    11,
    5,
    10,
    9
)
val SIZE_BITS_X = 1 + log2(smallestEncompassingPowerOfTwo(30000000))
val SIZE_BITS_Z = SIZE_BITS_X
val SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z
val BIT_SHIFT_Z = SIZE_BITS_Y
val BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z
val BITS_X = (1L shl SIZE_BITS_X) - 1L
val BITS_Y = (1L shl SIZE_BITS_Y) - 1L
val BITS_Z = (1L shl SIZE_BITS_Z) - 1L

fun log2(value: Int): Int {
    return log2DeBruijn(value) - if (isPowerOfTwo(value)) 0 else 1
}

fun isPowerOfTwo(value: Int): Boolean {
    return value != 0 && value and value - 1 == 0
}

fun smallestEncompassingPowerOfTwo(value: Int): Int {
    var i = value - 1
    i = i or (i shr 1)
    i = i or (i shr 2)
    i = i or (i shr 4)
    i = i or (i shr 8)
    i = i or (i shr 16)
    return i + 1
}

fun log2DeBruijn(value: Int): Int {
    var newValue = value
    newValue = if (isPowerOfTwo(value)) newValue else smallestEncompassingPowerOfTwo(newValue)
    return MULTIPLY_DE_BRUIJN_BIT_POSITION[(newValue.toLong() * 125613361L shr 27).toInt() and 31]
}

fun asLong(x: Int, y: Int, z: Int): Long {
    var long = 0L
    long = long or (x.toLong() and BITS_X shl BIT_SHIFT_X)
    long = long or  (y.toLong() and BITS_Y shl 0)
    long = long or (z.toLong() and BITS_Z shl BIT_SHIFT_Z)
    return long
}

fun unpackLongX(packedPos: Long) = (packedPos shl 64 - BIT_SHIFT_X - SIZE_BITS_X shr 64 - SIZE_BITS_X).toInt()

fun unpackLongY(packedPos: Long) = (packedPos shl 64 - SIZE_BITS_Y shr 64 - SIZE_BITS_Y).toInt()

fun unpackLongZ(packedPos: Long) = (packedPos shl 64 - BIT_SHIFT_Z - SIZE_BITS_Z shr 64 - SIZE_BITS_Z).toInt()