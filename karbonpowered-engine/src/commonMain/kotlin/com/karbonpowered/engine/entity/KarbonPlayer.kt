package com.karbonpowered.engine.entity

import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.scoreboard.ObjectiveCriteria
import com.karbonpowered.api.scoreboard.Scoreboard
import com.karbonpowered.api.scoreboard.ScoreboardObjective
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.component.KarbonPlayerNetworkComponent
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundMessagePacket
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundScoreboardDisplayPacket
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundSetScoreboardObjectivePacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KarbonPlayer(
    val session: KarbonSession,
    override val profile: GameProfile,
    override val world: KarbonWorld
) : KarbonHumanoid<Player>(), Player {
    val network = addComponent(KarbonPlayerNetworkComponent(session))
    override var scoreboard: Scoreboard? = null

    override fun displayScoreboardObjective(scoreboard: Scoreboard, objective: ScoreboardObjective, position: Scoreboard.Position) {
        this.scoreboard = scoreboard
        GlobalScope.launch {
            session.send(
                ClientboundSetScoreboardObjectivePacket(
                    objective.name,
                    objective.displayName,
                    ObjectiveCriteria.RenderType.INTEGER, // TODO
                    0 // TODO
                )
            )
            session.send(
                ClientboundScoreboardDisplayPacket(
                    position.ordinal, // TODO
                    scoreboard.name
                )
            )
        }
    }

    override fun hideScoreboardObjective() {
        GlobalScope.launch {
            session.send(
                ClientboundScoreboardDisplayPacket(
                    0,
                    ""
                )
            )
        }
    }

    override val type: EntityType<Player> = EntityType.PLAYER
    override var position: DoubleVector3
        get() = physics.position
        set(value) {
            physics.position = value
        }

    override fun sendMessage(source: UUID, message: Text, messageType: MessageType) {
        GlobalScope.launch {
            session.send(ClientboundMessagePacket(message, messageType, source))
        }
    }
}