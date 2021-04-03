package com.karbonpowered.io

import io.ktor.utils.io.core.*

interface Codec<T> {
    fun encode(output: Output, data: T)

    fun decode(input: Input): T
}