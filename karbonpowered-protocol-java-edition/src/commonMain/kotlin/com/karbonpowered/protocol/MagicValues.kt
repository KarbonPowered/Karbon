package com.karbonpowered.protocol

import com.karbonpowered.core.audience.MessageTypes
import com.karbonpowered.core.entity.living.player.ChatVisibilities
import com.karbonpowered.core.entity.living.player.GameModes
import com.karbonpowered.core.entity.living.player.HandTypes
import com.karbonpowered.core.entity.living.player.SkinParts
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundPlayerStatePacket
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

        register(ChatVisibilities.ENABLED, 0)
        register(ChatVisibilities.COMMANDS_ONLY, 1)
        register(ChatVisibilities.HIDDEN, 2)

        register(SkinParts.CAPE, 0x01)
        register(SkinParts.JACKET, 0x02)
        register(SkinParts.LEFT_SLEEVE, 0x04)
        register(SkinParts.RIGHT_SLEEVE, 0x08)
        register(SkinParts.LEFT_PANTS_LEG, 0x10)
        register(SkinParts.RIGHT_PANTS_LEG, 0x20)
        register(SkinParts.HAT, 0x40)

        register(HandTypes.OFF_HAND, 0)
        register(HandTypes.MAIN_HAND, 1)

        register(ServerboundPlayerStatePacket.PlayerState.START_SNEAKING, 0)
        register(ServerboundPlayerStatePacket.PlayerState.STOP_SNEAKING, 1)
        register(ServerboundPlayerStatePacket.PlayerState.LEAVE_BED, 2)
        register(ServerboundPlayerStatePacket.PlayerState.START_SPRINTING, 3)
        register(ServerboundPlayerStatePacket.PlayerState.STOP_SPRINTING, 4)
        register(ServerboundPlayerStatePacket.PlayerState.START_HORSE_JUMP, 5)
        register(ServerboundPlayerStatePacket.PlayerState.STOP_HORSE_JUMP, 6)
        register(ServerboundPlayerStatePacket.PlayerState.OPEN_HORSE_INVENTORY, 7)
        register(ServerboundPlayerStatePacket.PlayerState.START_ELYTRA_FLYING, 8)

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