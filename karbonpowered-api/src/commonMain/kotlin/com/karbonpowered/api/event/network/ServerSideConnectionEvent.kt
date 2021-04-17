package com.karbonpowered.api.event.network

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.entity.living.player.User
import com.karbonpowered.api.event.Cancellable
import com.karbonpowered.api.event.Event
import com.karbonpowered.api.event.message.MessageCancellable
import com.karbonpowered.api.event.message.MessageChannelEvent
import com.karbonpowered.api.event.message.MessageEvent
import com.karbonpowered.api.network.ServerSideConnection
import com.karbonpowered.api.world.server.ServerLocation
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.profile.GameProfile

interface ServerSideConnectionEvent : Event {
    val connection: ServerSideConnection
    val profile: GameProfile get() = connection.profile

    interface Auth : ServerSideConnectionEvent, MessageEvent, Cancellable

    interface Handshake : ServerSideConnectionEvent

    interface Login : ServerSideConnectionEvent, MessageEvent, Cancellable {
        val user: User
        val fromLocation: ServerLocation
        var toLocation: ServerLocation
        val fromRotation: DoubleVector3
        var toRotation: DoubleVector3
    }

    interface Join : ServerSideConnectionEvent, MessageChannelEvent, MessageCancellable {
        val player: Player
    }

    interface Disconnect : ServerSideConnectionEvent, MessageChannelEvent, MessageCancellable {
        val player: Player
    }
}