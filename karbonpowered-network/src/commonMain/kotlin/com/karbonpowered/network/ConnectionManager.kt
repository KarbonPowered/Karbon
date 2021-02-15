package com.karbonpowered.network

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*

/**
 * This class defines a basic structure for any object which manages connections.
 */
interface ConnectionManager : Closeable {
    /**
     * Creates a new Session for a [Connection]. This session will be used for all API-facing actions.
     * Therefore, this session will most likely be saved by the [ConnectionManager] in order to interact with the
     * [Session].
     *
     * @param connection the [Connection] the [Session] will be using
     * @return the new [Session]
     */
    fun newSession(connection: Connection): Session

    /**
     * Called when a [Session] becomes inactive because the underlying [Connection] has been closed.
     * All references to the [Session] should be removed, as it will no longer be valid.
     *
     * @param session the Session which will become inactive
     */
    fun sessionInactivated(session: Session)

    override fun close()
}