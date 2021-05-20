package com.karbonpowered.protocol

import com.karbonpowered.core.audience.MessageTypes
import com.karbonpowered.core.entity.living.player.GameModes
import kotlin.reflect.KClass

object MagicValues {
    val VALUES = mutableMapOf<Any, MutableList<Any>>()

    init {
        register(GameModes.SURVIVAL, 0)
        register(GameModes.CREATIVE, 1)
        register(GameModes.ADVENTURE, 2)
        register(GameModes.SPECTATOR, 3)
        register(GameModes.NOT_SET, 4)

        register(MessageTypes.ACTION_BAR, 0)
        register(MessageTypes.CHAT, 1)
        register(MessageTypes.SYSTEM, 2)

        register(MinecraftProtocol.SubProtocol.HANDSHAKE, 0)
        register(MinecraftProtocol.SubProtocol.STATUS, 1)
        register(MinecraftProtocol.SubProtocol.LOGIN, 2)
        register(MinecraftProtocol.SubProtocol.GAME, 3)
    }

    private fun register(key: Any, value: Any) {
        VALUES.getOrPut(key) { ArrayList() }.add(value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> key(keyType: KClass<T>, value: Any): T {
        for (key in VALUES.keys) {
            if (keyType.isInstance(key)) {
                for (v in VALUES[key] ?: throw UnmappedValueException(value, keyType)) {
                    if (v == value) {
                        return key as T
                    }
                }
            }
        }
        throw UnmappedValueException(value, keyType)
    }

    inline fun <reified T : Any> key(value: Any): T = key(T::class, value)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> value(valueType: KClass<T>, key: Any): T {
        if (VALUES.containsKey(key)) {
            for (value in VALUES[key] ?: throw UnmappedKeyException(key, valueType)) {
                if (value is Number) {
                    return when (valueType) {
                        Byte::class -> value.toByte()
                        Short::class -> value.toShort()
                        Int::class -> value.toInt()
                        Long::class -> value.toLong()
                        Double::class -> value.toDouble()
                        Float::class -> value.toFloat()
                        Boolean::class -> value.toByte() == 1.toByte()
                        else -> throw UnmappedKeyException(key, valueType)
                    } as T
                } else if (valueType.isInstance(value)) {
                    return value as T
                }
            }
        }
        throw UnmappedKeyException(key, valueType)
    }

    inline fun <reified T : Any> value(key: Any): T = value(T::class, key)

    data class UnmappedKeyException(
        val key: Any,
        val keyType: KClass<*>
    ) : IllegalArgumentException("Key $key has no mapping for key class $keyType")

    data class UnmappedValueException(
        val value: Any,
        val keyType: KClass<*>
    ) : IllegalArgumentException("Value $value has no mapping for key class $keyType")
}