package com.karbonpowered.common.math

/**
 * Rounds an integer up to the next power of 2.
 *
 * @param a The integer to round
 * @return the lowest power of 2 greater or equal to 'a'
 */
fun Int.roundUpPow2(): Int {
    var a = this
    return if (a <= 0) {
        1
    } else if (a > 0x40000000) {
        throw IllegalArgumentException("Rounding $a to the next highest power of two would exceed the int range")
    } else {
        a--
        a = a or (a shr 1)
        a = a or (a shr 2)
        a = a or (a shr 4)
        a = a or (a shr 8)
        a = a or (a shr 16)
        a++
        a
    }
}