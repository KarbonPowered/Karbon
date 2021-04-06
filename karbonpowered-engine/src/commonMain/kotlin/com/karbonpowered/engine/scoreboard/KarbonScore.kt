package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.scoreboard.Score
import com.karbonpowered.common.UUID

data class KarbonScore(
    override val name: String,
    override val owner: UUID? = null,
    override val value: Int = 0
) : Score