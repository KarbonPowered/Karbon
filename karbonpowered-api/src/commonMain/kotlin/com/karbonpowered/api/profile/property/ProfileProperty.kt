package com.karbonpowered.api.profile.property

interface ProfileProperty {
    val name: String
    val value: String
    val signature: String?
}