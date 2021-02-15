package com.karbonpowered.network

/**
 * Implementers of this class represent the data of a message to be sent.
 * There are a few rules that messages should follow:
 *
 * * All message fields should be immutable. This ensures thread-safety and makes it so Message objects can be safely
 * stored
 * * Message subclasses should override [toString], [equals], and [hashCode].
 * * All fields in a Message should be protocol-primitive (can be written directly via ByteBuf methods or via a
 * _single_ ByteBufUtils method)
 */
interface Message {
    override fun toString(): String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}