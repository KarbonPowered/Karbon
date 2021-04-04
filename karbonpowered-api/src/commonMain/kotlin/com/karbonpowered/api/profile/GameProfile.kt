package com.karbonpowered.api.profile

import com.karbonpowered.api.profile.property.ProfileProperty
import com.karbonpowered.common.UUID

interface GameProfile {
    val name: String?
    val uniqueId: UUID
    val properties: List<ProfileProperty>
}