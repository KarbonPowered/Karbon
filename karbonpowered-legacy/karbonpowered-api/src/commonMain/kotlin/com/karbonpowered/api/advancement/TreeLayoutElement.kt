package com.karbonpowered.api.advancement

import com.karbonpowered.math.vector.DoubleVector2

interface TreeLayoutElement {
    val advancement: Advancement
    var position: DoubleVector2
    fun setPosition(x: Double, y: Double)
}