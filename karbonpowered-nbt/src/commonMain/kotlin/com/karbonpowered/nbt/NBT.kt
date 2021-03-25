package com.karbonpowered.nbt

import com.karbonpowered.io.Codec
import io.ktor.utils.io.core.*

data class NBT(
    val name: String,
    private val tags: Map<String, Any>
) : Map<String, Any> by tags {
    constructor(tags: Map<String, Any>) : this("", tags)
    constructor(vararg tags: Pair<String, Any>) : this(tags.toMap())

    companion object : Codec<NBT?> {
        override suspend fun encode(output: Output, data: NBT?) {
            write(output, data)
        }

        override suspend fun decode(input: Input): NBT? {
            return read(input, input.readByte()) as? NBT
        }

        private fun read(input: Input, id: Byte): Any? = when (id.toInt()) {
            0 -> null
            1 -> input.readByte()
            2 -> input.readShort()
            3 -> input.readInt()
            4 -> input.readLong()
            5 -> input.readFloat()
            6 -> input.readDouble()
            7 -> ByteArray(input.readInt()) { input.readByte() }
            8 -> input.readText(max = input.readShort().toInt())
            9 -> {
                val listId = input.readByte()
                List(input.readInt()) {
                    read(input, listId)
                }
            }
            10 -> {
                val name = input.readText(max = input.readShort().toInt())
                val tags = mutableMapOf<String, Any>()
                while (true) {
                    val valueId = input.readByte()
                    if (valueId == 0.toByte()) break
                    if (valueId == 10.toByte()) {
                        val value = read(input, valueId) as NBT
                        tags[value.name] = value
                    } else {
                        val valueName = input.readText(max = input.readShort().toInt())
                        val value = read(input, valueId)
                        if (value != null) {
                            tags[valueName] = value
                        }
                    }
                }
                NBT(name, tags)
            }
            11 -> IntArray(input.readInt()) { input.readInt() }
            12 -> LongArray(input.readInt()) { input.readLong() }
            else -> error("Unknown tag with id $id")
        }

        private fun write(output: Output, value: Any?) {
            when (value) {
                null -> output.writeByte(0)
                is Byte -> output.writeByte(value)
                is Short -> output.writeShort(value)
                is Int -> output.writeInt(value)
                is Long -> output.writeLong(value)
                is Float -> output.writeFloat(value)
                is Double -> output.writeDouble(value)
                is ByteArray -> {
                    output.writeInt(value.size)
                    output.writeFully(value)
                }
                is String -> output.writeText(value)
                is Collection<*> -> {
                    output.writeByte(value.firstOrNull()?.let { idFor(it) } ?: 0)
                    output.writeInt(value.size)
                    value.forEach {
                        write(output, it!!)
                    }
                }
                is NBT -> {
                    output.writeText(value.name)
                    value.forEach { (nbtKey, nbtValue) ->
                        val id = idFor(value)
                        if (id != 10.toByte()) {
                            output.writeByte(id)
                            output.writeText(nbtKey)
                        }
                        write(output, nbtValue)
                    }
                    output.writeByte(0)
                }
                is IntArray -> {
                    output.writeInt(value.size)
                    value.forEach { output.writeInt(it) }
                }
                is LongArray -> {
                    output.writeInt(value.size)
                    value.forEach { output.writeLong(it) }
                }
            }
        }

        private fun idFor(value: Any): Byte = when (value) {
            is Byte -> 1
            is Short -> 2
            is Int -> 3
            is Long -> 4
            is Float -> 5
            is Double -> 6
            is ByteArray -> 7
            is String -> 8
            is Collection<*> -> 9
            is NBT -> 10
            is IntArray -> 11
            is LongArray -> 12
            else -> error("Unknown value type: $value")
        }
    }
}