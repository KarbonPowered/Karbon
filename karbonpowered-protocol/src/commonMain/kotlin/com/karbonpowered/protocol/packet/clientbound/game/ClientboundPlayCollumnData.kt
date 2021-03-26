package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.nbt.NBT
import com.karbonpowered.protocol.MinecraftPacket

data class ClientboundPlayCollumData(
    val x: Int,
    val z: Int,
    val heightMaps: NBT,
    val biomes: IntArray,
    val data: IntArray,
    val blockEntities: List<NBT>
) : MinecraftPacket {
}