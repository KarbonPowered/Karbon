package com.karbonpowered.nbt

import com.karbonpowered.io.Codec
import io.ktor.utils.io.core.*

data class NBT(
    val name: String,
    private val tags: Map<String, Any>
) : Map<String, Any> by tags {
    constructor(tags: Map<String, Any>) : this("", tags)
    constructor(vararg tags: Pair<String, Any>) : this(tags.toMap())

    override fun toString(): String {
        return buildString {
            append("{")
            tags.forEach {
                append("${it.key}: ${it.value}\n")
            }
            append("}")
        }
    }

    companion object : Codec<NBT?> {
        override suspend fun encode(output: Output, data: NBT?) {
            if (data == null) {
                output.writeByte(0)
            } else {
                output.writeByte(10)
                output.writeShort(0)
                write(output, data)
            }
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
                is Boolean -> output.writeByte(if (value) 1.toByte() else 0.toByte())
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
                is String -> {
                    output.writeShort(value.length.toShort())
                    output.writeFully(value.toByteArray())
                }
                is Collection<*> -> {
                    val collectionType = value.firstOrNull()?.let { idFor(it) } ?: 0
                    output.writeByte(collectionType)
                    output.writeInt(value.size)
                    value.forEach {
                        write(output, it!!)
                    }
                }
                is NBT -> {
                    value.forEach { (nbtKey, nbtValue) ->
                        val id = idFor(nbtValue)
                        output.writeByte(id)
                        output.writeShort(nbtKey.length.toShort())
                        output.writeFully(nbtKey.toByteArray())
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
            is Boolean,
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