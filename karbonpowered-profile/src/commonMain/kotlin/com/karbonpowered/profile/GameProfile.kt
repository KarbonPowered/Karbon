package com.karbonpowered.profile

import com.karbonpowered.common.Identifiable
import com.karbonpowered.common.UUID
import com.karbonpowered.data.DataSerializable

interface GameProfile : Identifiable, DataSerializable {
    override val uniqueId: UUID
    val name: String?
    val properties: Collection<Property>

    interface Property : DataSerializable {
        val name: String
        val value: String
        val signature: String?

        companion object {
            lateinit var factory: (String, String, String?) -> Property

            operator fun invoke(name: String, value: String, signature: String? = null) =
                factory(name, value, signature)
        }
    }

    companion object {
        lateinit var factory: (UUID, String?, Collection<Property>) -> GameProfile

        operator fun invoke(uniqueId: UUID, name: String? = null, properties: Collection<Property> = emptyList()) =
            factory(uniqueId, name, properties)
    }
}