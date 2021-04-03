package com.karbonpowered.engine.network

import com.karbonpowered.api.entity.living.player.GameModes
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.world.server.ServerLocation
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.engine.component.KarbonPlayerNetworkComponent
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.logging.Logger
import com.karbonpowered.math.vector.BaseMutableDoubleVector3
import com.karbonpowered.math.vector.doubleVector3of
import com.karbonpowered.nbt.NBT
import com.karbonpowered.network.NetworkServer
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGameJoinPacket
import io.ktor.network.sockets.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class KarbonServer : NetworkServer() {
    private val playersMap = mutableMapOf<Session, Player>()
    val maxPlayers = 100
    val players: Collection<Player> get() = playersMap.values

    override fun newSession(connection: Connection): Session = KarbonSession(
        connection, HandshakeProtocol(true)
    )

    override fun sessionInactivated(session: Session) {
        playersMap.remove(session)
    }

    val world = KarbonWorld().apply {
        GlobalScope.launch {
            while (true) {
                delay(1000/20)
                startTick()
            }
        }
    }

    suspend fun addPlayer(gameProfile: GameProfile, session: KarbonSession) {
        Logger.info("Connected: $gameProfile")
        val network = KarbonPlayerNetworkComponent(session)
        val player = KarbonPlayer(gameProfile, object : ServerLocation, BaseMutableDoubleVector3() {
            override val world: ServerWorld = this@KarbonServer.world
        })
        playersMap[session] = player
        session.send(createGameJoinPacket())
        player.addComponent(network)
        network.sendPositionUpdates(doubleVector3of(), doubleVector3of())
    }

    private fun createGameJoinPacket() = ClientboundGameJoinPacket(
        10, false, GameModes.CREATIVE, GameModes.CREATIVE,
        listOf("minecraft:world"),
        createDimensionCodec(),
        createOverworldTag(),
        "minecraft:world",
        0,
        100,
        10,
        false,
        true,
        false,
        true
    )

    fun createDimensionCodec() = NBT(
        "minecraft:dimension_type" to NBT(
            "type" to "minecraft:dimension_type",
            "value" to listOf(
                NBT(
                    "name" to "minecraft:overworld",
                    "id" to 0,
                    "element" to createOverworldTag()
                )
            )
        ),
        "worldgen/biome" to NBT(
            "type" to "minecraft:worldgen/biome",
            "value" to listOf(
                createBiome("plains", 0)
            )
        )
    )

    private fun createOverworldTag() = NBT(
        "piglin_safe" to false,
        "natural" to true,
        "ambient_light" to 1.0f,
        "infiniburn" to "minecraft:infiniburn_overworld",
        "respawn_anchor_works" to false,
        "has_skylight" to true,
        "bed_works" to true,
        "effects" to "minecraft:overworld",
        "has_raids" to true,
        "height" to 384,
        "min_y" to -64,
        "logical_height" to 384,
        "coordinate_scale" to 1.0,
        "ultrawarm" to false,
        "has_ceiling" to false
    )

    private fun createBiome(name: String, id: Int) = NBT(
        "name" to "minecraft:$name",
        "id" to id,
        "element" to NBT(
            "precipitation" to "rain",
            "effects" to createBiomeEffectTag(),
            "depth" to 0.125F,
            "temperature" to 0.8,
            "scale" to 0.05f,
            "downfall" to 0.4F,
            "category" to "plains"
        )
    )

    private fun createBiomeEffectTag() = NBT(
        "sky_color" to 7907327,
        "water_fog_color" to 329011,
        "fog_color" to 12638463,
        "water_color" to 4159204,
        "mood_sound" to NBT(
            "tick_delay" to 6000,
            "offset" to 2.0,
            "sound" to "minecraft:ambient.cave",
            "block_search_extent" to 8
        )
    )
}