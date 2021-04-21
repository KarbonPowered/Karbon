package com.karbonpowered.api.world.generation.config

interface WorldGenerationConfig {
    val seed: Long
    val generateFeatures: Boolean
    val generateBonusChest: Boolean

    interface Mutable : WorldGenerationConfig {
        override var seed: Long
        override var generateFeatures: Boolean
        override var generateBonusChest: Boolean

        interface Builder : com.karbonpowered.common.builder.Builder<Mutable, Builder> {
            var seed: Long
            var generateFeatures: Boolean
            var generateBonusChest: Boolean

            fun from(mutable: Mutable): Builder
        }
    }
}

