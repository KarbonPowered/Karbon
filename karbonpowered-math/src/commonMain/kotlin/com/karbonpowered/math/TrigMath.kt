package com.karbonpowered.math

import kotlin.math.floor
import kotlin.math.sin

// Constants
const val PI = kotlin.math.PI
const val TWO_PI = 2 * PI
const val DEGREES_TO_RADIANS = 0.017453292519943295
const val RADIANS_TO_DEGREES = 57.29577951308232

private const val SIN_BITS = 22
private const val SIN_SIZE = 1 shl SIN_BITS
private const val SIN_MASK = SIN_SIZE - 1
private val SIN_TABLE = FloatArray(SIN_SIZE) {
    sin((it * TWO_PI) / SIN_SIZE).toFloat()
}
private const val SIN_CONVERSION_FACTOR = SIN_SIZE / TWO_PI
private const val COS_OFFSET = SIN_SIZE / 4

fun tableSin(angle: Double) = sinRaw(floor(angle * SIN_CONVERSION_FACTOR).toInt())
fun tableCos(angle: Double) = cosRaw(floor(angle * SIN_CONVERSION_FACTOR).toInt())

private inline fun sinRaw(idx: Int) = SIN_TABLE[idx and SIN_MASK]
private inline fun cosRaw(idx: Int) = SIN_TABLE[(idx + COS_OFFSET) and SIN_MASK]

fun Double.toRadians() = this * DEGREES_TO_RADIANS
fun Double.toDegrees() = this * RADIANS_TO_DEGREES