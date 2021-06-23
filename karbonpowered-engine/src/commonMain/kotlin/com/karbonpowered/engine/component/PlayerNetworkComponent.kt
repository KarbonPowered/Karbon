package com.karbonpowered.engine.component

import com.karbonpowered.server.Session
import kotlinx.atomicfu.atomic

class PlayerNetworkComponent(
    session: Session
) : NetworkComponent() {
    var session by atomic(session)
}