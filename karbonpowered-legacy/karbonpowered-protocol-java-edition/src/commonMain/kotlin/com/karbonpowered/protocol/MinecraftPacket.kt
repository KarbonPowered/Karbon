package com.karbonpowered.protocol

import com.karbonpowered.server.packet.Packet

interface MinecraftPacket : Packet {
    override fun toString(): String
}