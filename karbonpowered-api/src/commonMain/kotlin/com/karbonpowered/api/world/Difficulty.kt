package com.karbonpowered.api.world

enum class Difficulty(val id: Int) {
    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3);

    companion object {
        fun byId(id: Int) = values().first { it.id == id }
    }
}