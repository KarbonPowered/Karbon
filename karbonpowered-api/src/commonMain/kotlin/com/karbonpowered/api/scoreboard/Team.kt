package com.karbonpowered.api.scoreboard

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.minecraft.text.format.Formatting

interface Team {
    val playerPrefix: Text
    val playerSuffix: Text
    val displayName: Text
    val allowFriendlyFire: Boolean
    val seeFriendlyInvisibles: Boolean
    val nameTagVisibility: Visibility
    val deathMessageVisibility: Visibility
    val color: Formatting
    val collisionRule: CollisionRule
    val name: String

    enum class Visibility(val ruleName: String) {
        ALWAYS("always"),
        NEVER("never"),
        HIDE_FOR_OTHER_TEAM("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam")
    }

    enum class CollisionRule(val ruleName: String){
        ALWAYS("always"),
        NEVER("never"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam")
    }
}