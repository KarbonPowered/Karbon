package com.karbonpowered.network.pipeline

import io.ktor.utils.io.core.*

interface Pipeline {
    suspend operator fun invoke(input: Input)

    suspend operator fun invoke(output: Output)
}