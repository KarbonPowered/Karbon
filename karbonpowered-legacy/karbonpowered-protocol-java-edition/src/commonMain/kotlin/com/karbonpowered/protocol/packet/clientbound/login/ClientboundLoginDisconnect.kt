package com.karbonpowered.protocol.packet.clientbound.login

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.writeString
import com.karbonpowered.text.LiteralText
import com.karbonpowered.text.Text
import com.karbonpowered.text.TextSerializers
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundLoginDisconnect(
    val message: Text = LiteralText("Disconnected")
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundLoginDisconnect> {
        override val packetType: KClass<ClientboundLoginDisconnect>
            get() = ClientboundLoginDisconnect::class

        override fun encode(output: Output, packet: ClientboundLoginDisconnect) {
            val serializedMessage = TextSerializers.JSON.serialize(packet.message)
            output.writeString(serializedMessage)
        }

        override fun decode(input: Input): ClientboundLoginDisconnect {
            val rawMessage = input.readString()
            val message = TextSerializers.JSON.deserialize(rawMessage)
            return ClientboundLoginDisconnect(message)
        }
    }
}