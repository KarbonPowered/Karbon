package com.karbonpowered.api.entity.living.player

import com.karbonpowered.common.UUID
import com.karbonpowered.profile.GameProfile
import com.karbonpowered.text.Text

interface PlayerList {
    var header: Text?
    var footer: Text?
    val entries: Collection<Entry>

    operator fun get(uniqueId: UUID): Entry?
    fun addEntry(entry: Entry)
    fun removeEntry(uniqueId: UUID): Entry?
    operator fun contains(uniqueId: UUID): Boolean = get(uniqueId) != null

    interface Entry {
        val list: PlayerList
        val profile: GameProfile
        var displayName: Text?
        var latency: Int
        var gameMode: GameMode

        interface Builder : com.karbonpowered.common.builder.Builder<Entry, Builder> {
            var list: PlayerList
            var profile: GameProfile
            var displayName: Text?
            var latency: Int
            var gameMode: GameMode

            companion object {
                lateinit var factory: () -> Builder

                operator fun invoke(builder: Builder.() -> Unit = {}) = factory().apply(builder).build()
            }
        }
    }
}