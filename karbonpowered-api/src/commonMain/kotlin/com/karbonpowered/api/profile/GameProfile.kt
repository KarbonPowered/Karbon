package com.karbonpowered.minecraft.api.profile

import com.karbonpowered.common.UUID

interface GameProfile {
    val name: String?
    val uniqueId: UUID
}