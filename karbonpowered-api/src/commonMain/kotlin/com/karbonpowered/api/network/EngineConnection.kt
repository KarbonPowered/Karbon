package com.karbonpowered.api.network

import com.karbonpowered.text.Text

interface EngineConnection : RemoteConnection {
    val side: EngineConnectionSide<out EngineConnection>

    override fun close()

    fun close(reason: Text)
}