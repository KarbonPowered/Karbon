package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.scoreboard.Score
import com.karbonpowered.api.scoreboard.ScoreboardObjective
import com.karbonpowered.api.scoreboard.Team
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGameScoreboardScorePacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class KarbonObjective(override val name: String) : ScoreboardObjective {
    override val scores = arrayListOf<Score>()
    override val whoCanSee = mutableListOf<KarbonPlayer>()
    override val players: List<String>
        get() = scores.map { it.name }

    override fun hasPlayerScore(name: String): Boolean {
        return players.contains(name)
    }

    override fun getPlayerScore(name: String): Score? {
        return scores.find { it.name == name }
    }

    override fun removePlayerScore(name: String): Boolean {
        ArrayList(scores).forEach {
            if (it.name == name) {
                return scores.remove(it).let { success ->
                    if (success) {
                        GlobalScope.launch { removeScoreboardScorePacket(name) }
                    }
                    success
                }
            }
        }
        return false
    }

    override fun setPlayerScore(name: String, value: Int, player: UUID?) {
        val score = KarbonScore(name, player, value)
        scores.add(score)
        setScoreboardScorePacket(score)
    }

    private fun removeScoreboardScorePacket(name: String) =
        scoreboardScorePacket(KarbonScore(name), ClientboundGameScoreboardScorePacket.ChangeMode.REMOVE)

    private fun setScoreboardScorePacket(score: Score) =
        scoreboardScorePacket(score, ClientboundGameScoreboardScorePacket.ChangeMode.CHANGE)

    private fun scoreboardScorePacket(score: Score, changeMode: ClientboundGameScoreboardScorePacket.ChangeMode) {
        GlobalScope.launch {
            whoCanSee.forEach {
                it.session.send(
                    ClientboundGameScoreboardScorePacket(
                        score.name,
                        name,
                        score.value,
                        changeMode
                    )
                )
            }
        }
    }
}