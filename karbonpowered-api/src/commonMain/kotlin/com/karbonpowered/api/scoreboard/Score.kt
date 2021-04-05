package com.karbonpowered.api.scoreboard

import com.karbonpowered.common.UUID

interface Score {
    val name: String
    val owner: UUID? // Score owner player unique id (optional). This is Karbon feature
}