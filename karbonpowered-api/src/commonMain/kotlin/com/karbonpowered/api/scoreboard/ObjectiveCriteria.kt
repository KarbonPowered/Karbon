package com.karbonpowered.api.scoreboard

abstract class ObjectiveCriteria {
    abstract val name: String
    open val readOnly: Boolean = false
    open val renderType: RenderType = RenderType.INTEGER

    enum class RenderType {
        INTEGER,
        HEARTS
    }
}