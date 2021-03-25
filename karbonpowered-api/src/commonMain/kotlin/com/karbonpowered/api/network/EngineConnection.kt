package com.karbonpowered.api.network

import com.karbonpowered.minecraft.text.Text
import io.ktor.utils.io.core.*

interface EngineConnection : Closeable {
    fun close(reason: Text)
}