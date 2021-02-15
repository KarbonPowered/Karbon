package com.karbonpowered.network.exception

/**
 * Thrown when a [com.karbonpowered.network.Codec] cannot be found for a given opcode and key.
 */
class UnknownPacketException(
    /** The message of this exception */
    override val message: String,
    /** The opcode of the unknown packet */
    val opcode: Int,
    /** The length of the packet, -1 if unknown */
    val length: Int
) : Exception()

class IllegalOpcodeException : Exception {
    constructor()
    constructor(message: String?) : super(message)
}

class ChannelClosedException : RuntimeException {
    constructor()
    constructor(message: String?) : super(message)
}