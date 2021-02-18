package com.karbonpowered.protocol

import com.karbonpowered.network.Message

interface MinecraftPacket : Message {
    override fun toString(): String
}