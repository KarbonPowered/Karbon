package com.karbonpowered.profile

import com.karbonpowered.common.Identifiable
import com.karbonpowered.common.UUID
import com.karbonpowered.data.DataContainer
import com.karbonpowered.data.DataSerializable
import com.karbonpowered.profile.GameProfileImpl.PropertyImpl

interface GameProfile : Identifiable, DataSerializable {
    override val uniqueId: UUID
    val name: String?
    val properties: Collection<Property>

    interface Property : DataSerializable {
        val name: String
        val value: String
        val signature: String?

        companion object {
            var factory: (String, String, String?) -> Property = ::PropertyImpl

            operator fun invoke(name: String, value: String, signature: String? = null) =
                factory(name, value, signature)
        }
    }

    companion object {
        var factory: (UUID, String?, Collection<Property>) -> GameProfile = ::GameProfileImpl

        operator fun invoke(uniqueId: UUID, name: String? = null, properties: Collection<Property> = emptyList()) =
            factory(uniqueId, name, properties)
    }
}

internal data class GameProfileImpl(
    override val uniqueId: UUID,
    override val name: String?,
    override val properties: Collection<GameProfile.Property>
) : GameProfile {
    override fun toContainer(): DataContainer {
        TODO("Not yet implemented")
    }

    internal data class PropertyImpl(
        override val name: String,
        override val value: String,
        override val signature: String?
    ) : GameProfile.Property {
        override fun toContainer(): DataContainer {
            TODO("Not yet implemented")
        }
    }
}