package com.karbonpowered.engine.protocol.event

import com.karbonpowered.engine.protocol.ProtocolEvent
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.server.Session

class ChunkSendEvent(
    override val session: Session,
    val chunk: KarbonChunk
) : ProtocolEvent