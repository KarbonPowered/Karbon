package com.karbonpowered.vanilla.player

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.player.KarbonPlayer
import com.karbonpowered.engine.player.PlayerNetwork
import com.karbonpowered.server.Session

class VanillaPlayer(
    engine: KarbonEngine,
    uniqueId: UUID,
    username: String,
    session: Session
) : KarbonPlayer(engine, uniqueId, username, session) {
    override val network: PlayerNetwork = VanillaPlayerNetwork(this, session)
}