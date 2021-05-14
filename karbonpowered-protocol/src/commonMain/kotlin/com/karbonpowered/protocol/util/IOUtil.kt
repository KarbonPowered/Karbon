package com.karbonpowered.protocol.util

import com.karbonpowered.common.UUID
import com.karbonpowered.math.asLong
import com.karbonpowered.nbt.NBT
import io.ktor.utils.io.core.*

fun Input.readUUID(): UUID {
    val mostSignificantBits = readLong()
    val leastSignificantBits = readLong()
    return UUID(mostSignificantBits, leastSignificantBits)
}

fun Output.writeUUID(uniqueId: UUID) {
    writeLong(uniqueId.mostSignificantBits)
    writeLong(uniqueId.leastSignificantBits)
}

fun Output.writeNBT(nbt: NBT?) = NBT.encode(this, nbt)
fun Input.readNBT(): NBT? = NBT.decode(this)

fun Output.writeBlockPosition(x: Int, y: Int, z: Int) = writeLong(asLong(x, y, z))