package com.karbonpowered.server.event.server

interface ServerListener {
    fun sessionAdded(event: SessionAddedEvent) {}
    fun sessionRemoved(event: SessionRemovedEvent) {}
    fun serverClosing(event: ServerClosingEvent) {}
    fun serverClosed(event: ServerClosedEvent) {}
}