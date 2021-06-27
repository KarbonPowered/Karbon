package com.karbonpowered.engine.world.discrete

interface TransformProvider {
    val transform: Transform
}

object NullTransformProvider : TransformProvider {
    override val transform = Transform.INVALID
}