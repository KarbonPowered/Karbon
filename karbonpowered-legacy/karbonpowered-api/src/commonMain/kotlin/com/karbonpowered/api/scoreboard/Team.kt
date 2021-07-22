package com.karbonpowered.api.scoreboard

import com.karbonpowered.text.NamedTextColor
import com.karbonpowered.text.Text

interface Team {
    val name: String
    var displayName: Text
    var color: NamedTextColor
    var prefix: Text
    var suffix: Text
    var isAllowFriendlyFire: Boolean
    var canSeeFriendlyInvisibles: Boolean
    var nameTagVisibility: NameTagVisibility
    var deathMessageVisibility: NameTagVisibility
    var collisionRule: CollisionRule
    val members: Set<Text>
    fun addMember(member: Text)
    fun removeMember(member: Text)
    val scoreboard: Scoreboard?
    fun unregister()

    interface Builder : com.karbonpowered.common.builder.Builder<Team, Builder> {
        var name: String
        var color: NamedTextColor
        var displayName: Text
        var prefix: Text
        var suffix: Text
        var isAllowFriendlyFire: Boolean
        var canSeeFriendlyInvisibilities: Boolean
        var nameTagVisibility: NameTagVisibility
        var deathTextVisibility: NameTagVisibility
        var collisionRule: CollisionRule
        var members: MutableSet<Text>
    }
}