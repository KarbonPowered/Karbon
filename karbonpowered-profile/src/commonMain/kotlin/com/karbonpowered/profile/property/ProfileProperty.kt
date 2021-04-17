package com.karbonpowered.profile.property

import com.karbonpowered.data.DataContainer
import com.karbonpowered.data.DataQuery
import com.karbonpowered.data.DataSerializable

interface ProfileProperty : DataSerializable {
    val name: String
    val value: String
    val signature: String?

    fun hasSignature(): Boolean = signature != null

    interface Factory {
        fun create(name: String, value: String, signature: String? = null): ProfileProperty
    }

    companion object {
        const val TEXTURES = "textures"
        var factory: Factory = KarbonProfileProperty.Companion

        fun of(name: String, value: String, signature: String? = null): ProfileProperty =
                factory.create(name, value, signature)
    }
}

inline fun ProfileProperty(name: String, value: String, signature: String? = null): ProfileProperty =
        ProfileProperty.of(name, value, signature)

data class KarbonProfileProperty(
        override val name: String,
        override val value: String,
        override val signature: String?
) : ProfileProperty {
    override fun toContainer(): DataContainer = DataContainer {
        set(NAME, name)
        set(VALUE, value)
        if (signature != null) {
            set(SIGNATURE, signature)
        }
    }

    companion object : ProfileProperty.Factory {
        val VALUE = DataQuery("Value")
        val SIGNATURE = DataQuery("Signature")
        val NAME = DataQuery("name")

        override fun create(name: String, value: String, signature: String?): ProfileProperty =
                KarbonProfileProperty(name, value, signature)
    }
}