package com.karbonpowered.server

import com.karbonpowered.server.event.server.*

abstract class AbstractServer(
    override val host: String,
    override val port: Int
) : Server {
    private val _sessions = ArrayList<Session>()
    private val _listeners = ArrayList<ServerListener>()
    override val listeners: Collection<ServerListener>
        get() = _listeners

    override val sessions: Collection<Session> = ArrayList(_sessions)

    fun addSession(session: Session) {
        _sessions.add(session)
        callEvent(SessionAddedEvent(this, session))
    }

    fun removeSession(session: Session) {
        _sessions.remove(session)
        if (session.isConnected) {
            session.disconnect("Connection closed.")
        }
        callEvent(SessionRemovedEvent(this, session))
    }

    override fun addListener(listener: ServerListener) {
        _listeners.add(listener)
    }

    override fun removeListener(listener: ServerListener) {
        _listeners.remove(listener)
    }

    protected open fun callEvent(event: ServerEvent) {
        for (listener in listeners) {
            event.call(listener)
        }
    }

    override suspend fun close() {
        callEvent(ServerClosingEvent(this))
        sessions.forEach { session ->
            if (session.isConnected) {
                session.disconnect("Server closed")
            }
        }
        closeImpl()
        callEvent(ServerClosedEvent(this))
    }

    protected abstract suspend fun closeImpl()
}