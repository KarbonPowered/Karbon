package com.karbonpowered.engine.profile

import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.profile.property.ProfileProperty
import com.karbonpowered.common.UUID

data class KarbonGameProfile(
    override val name: String?,
    override val uniqueId: UUID,
    override val properties: List<ProfileProperty>
) : GameProfile {
    override fun toString(): String = "($name $uniqueId $properties)"
}