package com.karbonpowered.engine.profile.property

import com.karbonpowered.api.profile.property.ProfileProperty

data class KarbonProfileProperty(
    override val name: String,
    override val value: String,
    override val signature: String?
) : ProfileProperty {
    override fun toString() = "$name = $value (signature=$signature)"
}
