package com.karbonpowered.math

import com.karbonpowered.math.vector.FloatVector3
import com.karbonpowered.math.vector.FloatVector4

class Transform(
    var position: FloatVector3,
    var orientation: FloatVector4
) {
    fun copy(): Transform = Transform(position, orientation)
}