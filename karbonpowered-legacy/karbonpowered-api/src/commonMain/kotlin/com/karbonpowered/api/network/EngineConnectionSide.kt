package com.karbonpowered.api.network

class EngineConnectionSide<C : EngineConnection> private constructor() {
    companion object {
        /**
         * The client side.
         */
        val CLIENT = EngineConnectionSide<ClientSideConnection>()

        /**
         * The server side.
         */
        val SERVER = EngineConnectionSide<ServerSideConnection>()
    }
}
