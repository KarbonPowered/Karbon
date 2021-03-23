package com.karbonpowered.io

import io.ktor.utils.io.core.*

interface Codec<T> {
    suspend fun encode(output: Output, data: T)

    suspend fun decode(input: Input): T
}