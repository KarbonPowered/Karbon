package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.scoreboard.PlayerTeam
import com.karbonpowered.api.scoreboard.Team
import com.karbonpowered.engine.Engine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.minecraft.text.LiteralText
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.minecraft.text.format.Formatting
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundSetPlayerTeamPacket
import com.karbonpowered.protocol.packet.clientbound.game.SerializableTeam
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KarbonTeam(override val name: String) : PlayerTeam() {
    override var allowFriendlyFire: Boolean = true
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var collisionRule: Team.CollisionRule = Team.CollisionRule.ALWAYS
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var color: Formatting = Formatting.WHITE
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var deathMessageVisibility: Team.Visibility = Team.Visibility.ALWAYS
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var displayName: Text = LiteralText(name)
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var nameTagVisibility: Team.Visibility = Team.Visibility.ALWAYS
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var playerPrefix: Text = LiteralText("")
        set(value) {
            field = value
            sendModifyPacket()
        }
    override var playerSuffix: Text = LiteralText("")
        set(value) {
            field = value
            sendModifyPacket()
        }
    override val players = mutableListOf<GameProfile>()

    override fun addPlayer(player: Player): Boolean {
        return addPlayer(player.profile)
    }

    override val seeFriendlyInvisibles: Boolean = false

    override fun addPlayer(gameProfile: GameProfile): Boolean {
        val success = players.add(gameProfile)
        sendPlayerPacket(false)
        return success
    }

    override fun removePlayer(player: Player): Boolean {
        return removePlayer(player.profile)
    }

    override fun removePlayer(gameProfile: GameProfile): Boolean {
        val success = players.remove(gameProfile)
        sendPlayerPacket(true)
        return success
    }

    private fun teamPacket(player: KarbonPlayer, changeMode: Int) {
        GlobalScope.launch {
            player.session.send(
                ClientboundSetPlayerTeamPacket(
                    changeMode,
                    name,
                    if (changeMode != 1) players.map { it.name ?: "" } else emptyList(),
                    if (changeMode != 1) serializable() else null
                )
            )
        }
    }

    fun sendCreatePacket() {
        Engine.server.players.forEach {
            teamPacket(it as KarbonPlayer, 0)
        }
    }

    fun sendModifyPacket() {
        Engine.server.players.forEach {
            teamPacket(it as KarbonPlayer, 2)
        }
    }

    fun sendRemovePacket() {
        Engine.server.players.forEach {
            teamPacket(it as KarbonPlayer, 1)
        }
    }

    private fun sendPlayerPacket(remove: Boolean) =
        Engine.server.players.forEach {
            teamPacket(it as KarbonPlayer, if (remove) 4 else 3)
        }

    private fun packOptions(): Int {
        var i = 0
        if (allowFriendlyFire) {
            i = i or 1
        }

        if (seeFriendlyInvisibles) {
            i = i or 2
        }

        return i
    }

    private fun serializable() = SerializableTeam(
        displayName,
        playerPrefix,
        playerSuffix,
        nameTagVisibility.ruleName,
        collisionRule.ruleName,
        color,
        packOptions()
    )
}