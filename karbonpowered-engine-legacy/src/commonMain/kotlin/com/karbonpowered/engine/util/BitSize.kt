package com.karbonpowered.engine.util

class BitSize(val bits: Int) {
    var size = 1 shl bits
    var area = size * size
    var volume = area * size
    var halfSize = size shr 1
    var HALF_AREA = area shr 1
    var HALF_VOLUME = volume shr 1
    var DOUBLE_SIZE = size shl 1
    var DOUBLE_AREA = area shl 1
    var DOUBLE_VOLUME = volume shl 1
    var doubleBits = bits shl 1
    var mask = size - 1
}
