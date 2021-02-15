package com.karbonpowered.network

/**
 * Represents a connection to another engine.
 *
 * Controls the state, protocol and channels of a connection to another engine.
 */
interface Session {
    /**
     * Gets the protocol associated with this session.
     */
    val protocol: Protocol

    /**
     * Passes a message to a session for processing.
     *
     * @param message message to be processed
     */
    fun <T : Message> messageReceived(message: T)

    /**
     * Sends a message across the network.
     *
     * @param message The message.
     */
    fun send(message: Message)

    /**
     * Sends any amount of messages to the client.
     *
     * @param messages the messages to send to the client
     */
    fun sendAll(vararg messages: Message)

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