package com.karbonpowered.api.network

interface ClientSideConnection : EngineConnection {
    override val side: EngineConnectionSide<out EngineConnection>
        get() = EngineConnectionSide.CLIENT
}