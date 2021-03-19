package com.karbonpowered.network

import com.karbonpowered.network.protocol.Protocol
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

/**
 * Represents a connection to another engine.
 *
 * Controls the state, protocol and channels of a connection to another engine.
 */
interface Session {
    /**
     * Gets the protocol associated with this session.
     */
    var protocol: Protocol

    val connection: Connection

    /**
     * Passes a message to a session for processing.
     *
     * @param message message to be processed
     */
    suspend fun <T : Message> messageReceived(message: T)

    /**
     * Sends a message across the network.
     *
     * @param message The message.
     */
    suspend fun send(vararg messages: Message)

    /**
     * Closes the session.
     */
    fun disconnect()

    /**
     * Called after the Session has been disconnected, right before the Session is invalidated.
     */
    fun onDisconnect()

    /**
     * Called once the Session is ready for messages.
     */
    fun onReady()

    /**
     * Called when a throwable is thrown in the pipeline during inbound operations.
     *
     * @param throwable The throwable that was thrown
     */
    fun onInboundThrowable(throwable: Throwable)
}