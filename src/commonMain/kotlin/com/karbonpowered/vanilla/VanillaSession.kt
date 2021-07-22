package com.karbonpowered.vanilla

import com.karbonpowered.vanilla.network.VanillaProtocol
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.network.sockets.*
import kotlinx.coroutines.channels.Channel

data class VanillaSession(
    val connection: Connection,
    val protocol: VanillaProtocol,
    val sendChannel: Channel<VanillaPacket> = Channel()
) {
    suspend fun send(packet: VanillaPacket) = sendChannel.send(packet)
}