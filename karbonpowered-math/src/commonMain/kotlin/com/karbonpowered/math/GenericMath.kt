package com.karbonpowered.math

/**
 * A "close to zero" float epsilon value for use
 */
val FLT_EPSILON = Float.fromBits(0x34000000)

fun Int.multiplyToShift(): Int {
    require(this >= 1) { "Multiplicand must be at least 1" }
    val shift = 31 - numberOfLeadingZeros
    require(1 shl shift == this) { "Multiplicand must be a power of 2" }
    return shift
}

val Int.numberOfLeadingZeros: Int
    get() {
        // HD, Count leading 0's
        var i = this
        if (i <= 0) return if (i == 0) 32 else 0
        var n = 31
        if (i >= 1 shl 16) {
            n -= 16
            i = i ushr 16
        }
        if (i >= 1 shl 8) {
            n -= 8
            i = i ushr 8
        }
        if (i >= 1 shl 4) {
            n -= 4
            i = i ushr 4
        }
        if (i >= 1 shl 2) {
            n -= 2
            i = i ushr 2
        }
        return n - (i ushr 1)
    }
