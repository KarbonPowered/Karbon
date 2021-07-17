package com.karbonpowered.vanilla.player

import com.karbonpowered.common.hash.IntPairHashed
import com.karbonpowered.core.entity.living.player.GameModes
import com.karbonpowered.engine.player.PlayerNetwork
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.reference.WorldReference
import com.karbonpowered.protocol.packet.clientbound.game.*
import com.karbonpowered.server.Session
import com.karbonpowered.vanilla.world.VanillaWorld
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.async
import kotlin.random.Random

private const val COLUMN_HEIGHT = 24

class VanillaPlayerNetwork(
    override val player: VanillaPlayer,
    session: Session,
) : PlayerNetwork(player, session) {
    private var currentWorld by atomic<WorldReference?>(null)
    private val activeColumns = mutableSetOf<Long>()
    private var previousSyncDistance by atomic(0)

    // TODO: Optimize chunk sending
    override suspend fun attemptSendChunk(chunk: KarbonChunk): Boolean {
        val columnKey = IntPairHashed.key(chunk.chunkX, chunk.chunkZ)
        if (activeColumns.contains(columnKey)) {
            return true
        } else {
            activeColumns.add(columnKey)
            val world = chunk.world.refresh(player.engine.worldManager)
            val chunks = Array(COLUMN_HEIGHT) { idx ->
                session.async {
                    world?.getChunk(
                        chunk.chunkX,
                        idx - 4,
                        chunk.chunkZ,
                        LoadOption.LOAD_GEN
                    )
                }
            }

            val chunkData = Array(COLUMN_HEIGHT) { idx ->
                val c = chunks[idx].await()
                if (c != null) {
                    ClientboundPlayColumnData.ChunkData().also { chunkData ->
                        repeat(16) { x ->
                            repeat(16) { y ->
                                repeat(16) { z ->
                                    chunkData[x, y, z] = c.blockStore[x, y, z]
                                }
                            }
                        }
                    }
                } else null
            }

            val packet = ClientboundPlayColumnData(
                chunk.chunkX,
                chunk.chunkZ,
                chunks = chunkData
            )
            session.sendPacket(packet)
        }
        return true
    }

    override fun sendPositionUpdates(transform: Transform) {
        var flush = false
        if (currentWorld != transform.position.world) {
            val world = requireNotNull(transform.position.world.refresh(player.engine.worldManager))
            val dimensionCodec = VanillaWorld.createDimensionCodec()
            val dimension = VanillaWorld.createOverworldTag()
            if (currentWorld == null) {
                session.sendPacket(
                    ClientboundGameJoinPacket(
                        0,
                        false,
                        GameModes.CREATIVE,
                        GameModes.NOT_SET,
                        listOf(transform.position.world.identifier.toString()),
                        dimensionCodec,
                        dimension,
                        transform.position.world.identifier.toString(),
                        Random(world.seed.hashCode()).nextLong(),
                        100,
                        10,
                        false,
                        false,
                        false,
                        true
                    ), flush = false
                )
                session.sendPacket(
                    ClientboundSyncDistancePacket(16),
                    flush = false
                )
                if (previousTransform.position != transform.position) {
                    session.sendPacket(
                        ClientboundGamePlayerPositionRotationPacket(
                            transform.position.x.toDouble(),
                            transform.position.y.toDouble(),
                            transform.position.z.toDouble(),
                            transform.rotation.x,
                            transform.rotation.y
                        ), flush = false
                    )
                }
                flush = true
            }

            currentWorld = transform.position.world
        }
        if (previousTransform.position.chunkX != transform.position.chunkX ||
            previousTransform.position.chunkZ != transform.position.chunkZ
        ) {
            session.sendPacket(
                ClientboundSyncPositionPacket(
                    transform.position.chunkX,
                    transform.position.chunkZ
                ), flush = false
            )
        }
        if (flush) {
            session.flushPackets()
        }
    }
}