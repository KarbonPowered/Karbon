package com.karbonpowered.profile

import com.karbonpowered.common.Identifiable
import com.karbonpowered.common.UUID
import com.karbonpowered.common.md5
import com.karbonpowered.common.uuidOf
import com.karbonpowered.data.DataContainer
import com.karbonpowered.data.DataQuery
import com.karbonpowered.data.DataSerializable
import com.karbonpowered.profile.property.ProfileProperty
import io.ktor.utils.io.core.*
import kotlin.experimental.and
import kotlin.experimental.or

interface GameProfile : Identifiable, DataSerializable {
    override val uniqueId: UUID
    val name: String?
    val properties: Collection<ProfileProperty>

    fun hasName(): Boolean = name != null

    fun withName(name: String?): GameProfile

    fun withProperty(property: ProfileProperty): GameProfile
    fun withProperties(vararg properties: ProfileProperty): GameProfile
    fun withProperties(properties: Iterable<ProfileProperty>): GameProfile

    fun withoutProperties(): GameProfile
    fun withoutProperties(vararg properties: ProfileProperty): GameProfile
    fun withoutProperties(properties: Iterable<ProfileProperty>): GameProfile
    fun withoutProperties(name: String): GameProfile = withoutProperties { it.name == name }
    fun withoutProperties(filter: (ProfileProperty) -> Boolean): GameProfile

    interface Factory {
        fun create(uniqueId: UUID, name: String? = null, properties: Collection<ProfileProperty>): GameProfile
    }

    companion object {
        var factory: Factory = KarbonGameProfile.Companion

        fun offlineUniqueId(name: String): UUID {
            val md5bytes = "OfflinePlayer:$name".toByteArray().md5()
            md5bytes[6] = md5bytes[6] and 0x0f
            md5bytes[6] = md5bytes[6] or 0x30
            md5bytes[8] = md5bytes[8] and 0x3f
            md5bytes[8] = md5bytes[8] or 0x80.toByte()
            return uuidOf(md5bytes)
        }

        fun of(
            uniqueId: UUID,
            name: String? = null,
            properties: Collection<ProfileProperty> = emptyList()
        ): GameProfile = factory.create(uniqueId, name, properties)
    }
}

inline fun GameProfile(
    uniqueId: UUID,
    name: String? = null,
    properties: Collection<ProfileProperty> = emptyList()
): GameProfile = GameProfile.of(uniqueId, name, properties)

data class KarbonGameProfile(
    override val uniqueId: UUID,
    override val name: String? = null,
    override val properties: Collection<ProfileProperty> = emptyList()
) : GameProfile {
    override fun toContainer(): DataContainer = DataContainer {
        set(UUID, uniqueId)
        set(NAME, this.name)
        if (properties.isNotEmpty()) {
            set(PROPERTIES, properties.map { it.toContainer() })
        }
    }

    override fun withName(name: String?): GameProfile =
        KarbonGameProfile(uniqueId, name, properties)

    override fun withProperty(property: ProfileProperty): GameProfile =
        KarbonGameProfile(uniqueId, name, properties + property)

    override fun withProperties(vararg properties: ProfileProperty): GameProfile =
        KarbonGameProfile(uniqueId, name, this.properties + properties)

    override fun withProperties(properties: Iterable<ProfileProperty>): GameProfile =
        KarbonGameProfile(uniqueId, name, this.properties + properties)

    override fun withoutProperties(): GameProfile =
        KarbonGameProfile(uniqueId, name)

    override fun withoutProperties(vararg properties: ProfileProperty): GameProfile =
        KarbonGameProfile(uniqueId, name, this.properties.filter { !this.properties.contains(it) })

    override fun withoutProperties(properties: Iterable<ProfileProperty>): GameProfile =
        KarbonGameProfile(uniqueId, name, this.properties.filter { !this.properties.contains(it) })

    override fun withoutProperties(filter: (ProfileProperty) -> Boolean): GameProfile =
        KarbonGameProfile(uniqueId, name, this.properties.filter(filter))

    companion object : GameProfile.Factory {
        val UUID = DataQuery("UUID")
        val NAME = DataQuery("Name")
        val PROPERTIES = DataQuery("Properties")

        override fun create(uniqueId: UUID, name: String?, properties: Collection<ProfileProperty>): GameProfile =
            KarbonGameProfile(uniqueId, name, properties)
    }
}