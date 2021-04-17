package com.karbonpowered.profile

import com.karbonpowered.common.UUID

interface GameProfileProvider {
    suspend fun basicProfile(uniqueId: UUID): GameProfile

    suspend fun basicProfile(name: String, timeMs: Long? = null): GameProfile

    suspend fun basicProfiles(names: Iterable<String>, timeMs: Long? = null): Map<String, GameProfile>

    suspend fun profile(profile: GameProfile, signed: Boolean = true): GameProfile =
            profile(profile.uniqueId, signed)

    suspend fun profile(name: String, signed: Boolean = true): GameProfile

    suspend fun profile(uniqueId: UUID, signed: Boolean = true): GameProfile
}