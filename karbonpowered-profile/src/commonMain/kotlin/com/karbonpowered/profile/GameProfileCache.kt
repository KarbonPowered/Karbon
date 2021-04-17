package com.karbonpowered.profile

import com.karbonpowered.common.UUID

interface GameProfileCache : Iterable<GameProfile> {
    fun remove(profile: GameProfile): Boolean
    fun remove(vararg profiles: GameProfile): Collection<GameProfile>
    fun remove(profiles: Iterable<GameProfile>): Collection<GameProfile>
    fun removeIf(filter: (GameProfile) -> Boolean): Collection<GameProfile>

    fun clear()

    fun byId(uniqueId: UUID): GameProfile?
    fun byIds(vararg uniqueIds: UUID): Map<UUID, GameProfile>
    fun byIds(uniqueIds: Iterable<UUID>): Map<UUID, GameProfile>

    fun byName(name: String): GameProfile?
    fun byNames(vararg names: String): Map<String, GameProfile>
    fun byNames(names: Iterable<String>): Map<String, GameProfile>
}