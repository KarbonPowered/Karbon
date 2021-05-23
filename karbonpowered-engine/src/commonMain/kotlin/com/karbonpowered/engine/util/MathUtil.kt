@file:Suppress("NOTHING_TO_INLINE")

package com.karbonpowered.engine.util

/**
 * Rounds an integer up to the next power of 2.
 * @param x
 * @return the lowest power of 2 greater or equal to x
 */
inline fun Int.roundUpPow2(): Int {
    var i = this
    return when {
        i <= 0 -> 1
        i > 0x40000000 -> throw IllegalArgumentException(
            "Rounding $i to the next highest power of two would exceed the int range"
        )
        else -> {
            i--
            i = i or (i shr 1)
            i = i or (i shr 2)
            i = i or (i shr 4)
            i = i or (i shr 8)
            i = i or (i shr 16)
            i++
            i
        }
    }
}
