package com.karbonpowered.engine.world

import com.karbonpowered.api.Engine
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.block.entity.BlockEntity
import com.karbonpowered.api.component.Component
import com.karbonpowered.api.context.Context
import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.server.ServerPlayer
import com.karbonpowered.api.fluid.FluidState
import com.karbonpowered.api.registry.RegistryHolder
import com.karbonpowered.api.registry.RegistryScope
import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.api.world.biome.Biome
import com.karbonpowered.api.world.block.BlockChangeFlag
import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.api.world.volume.sequence.SequenceOptions
import com.karbonpowered.api.world.volume.sequence.VolumeSequence
import com.karbonpowered.api.world.weather.Weather
import com.karbonpowered.api.world.weather.WeatherType
import com.karbonpowered.common.UUID
import com.karbonpowered.component.BaseComponentOwner
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3
import kotlin.random.Random
import kotlin.reflect.KClass

class KarbonWorld : BaseComponentOwner(), ServerWorld {
    override val players: Collection<ServerPlayer>
        get() = TODO("Not yet implemented")
    override val entities: Collection<Entity<*>>
        get() = TODO("Not yet implemented")
    override val key: ResourceKey
        get() = TODO("Not yet implemented")
    override val isLoaded: Boolean
        get() = TODO("Not yet implemented")
    override val loadedChunks: Iterable<Chunk>
        get() = TODO("Not yet implemented")

    override val engine: Engine
        get() = TODO("Not yet implemented")
    override val seed: Long
        get() = TODO("Not yet implemented")
    override val difficulty: com.karbonpowered.api.world.difficulty.Difficulty
        get() = TODO("Not yet implemented")

//    override val blockMin: IntVector3 = intVector3Of(-30000000, -64, -30000000)
//    override val blockMax: IntVector3 = intVector3Of(30000000, 384, 30000000)
//    override val blockSize: IntVector3 = blockMax - blockMin + intVector3Of(1)

    override val blockEntities: Collection<BlockEntity>
        get() = TODO("Not yet implemented")
    override val random: Random
        get() = TODO("Not yet implemented")

    override val context: Context
        get() = TODO("Not yet implemented")

    override var weather: Weather
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun region(x: Int, y: Int, z: Int, loadOption: WorldLoadOption) {
        TODO("Not yet implemented")
    }

    override fun chunk(x: Int, y: Int, z: Int, loadOnly: WorldLoadOption) {
        TODO("Not yet implemented")
    }

    override val registryScope: RegistryScope
        get() = TODO("Not yet implemented")
    override val registries: RegistryHolder
        get() = TODO("Not yet implemented")
    override val uniqueId: UUID
        get() = TODO("Not yet implemented")

    override fun setBiome(x: Int, y: Int, z: Int, biome: Biome): Boolean {
        TODO("Not yet implemented")
    }

    override fun biomeSequence(
        min: IntVector3,
        max: IntVector3,
        option: SequenceOptions
    ): VolumeSequence<ServerWorld, Biome> {
        TODO("Not yet implemented")
    }

    override fun biome(x: Int, y: Int, z: Int): Biome {
        TODO("Not yet implemented")
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: BlockState): Boolean {
        TODO("Not yet implemented")
    }

    override fun setBlock(x: Int, y: Int, z: Int, state: BlockState, flag: BlockChangeFlag): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeBlock(x: Int, y: Int, z: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun <E : Entity<E>> createEntity(type: EntityType<E>, position: DoubleVector3): E {
        TODO("Not yet implemented")
    }

    override fun <E : Entity<E>> createEntityNaturally(type: EntityType<E>, position: DoubleVector3): E {
        TODO("Not yet implemented")
    }

    override fun spawnEntity(entity: Entity<*>): Boolean {
        TODO("Not yet implemented")
    }

    override fun spawnEntities(entities: Iterable<Entity<*>>): Collection<Entity<*>> {
        TODO("Not yet implemented")
    }

    override fun entitySequence(
        min: IntVector3,
        max: IntVector3,
        options: SequenceOptions
    ): VolumeSequence<ServerWorld, Entity<*>> {
        TODO("Not yet implemented")
    }

    override fun entity(uuid: UUID): Entity<*>? {
        TODO("Not yet implemented")
    }

    override fun addBlockEntity(x: Int, y: Int, z: Int, blockEntity: BlockEntity) {
        TODO("Not yet implemented")
    }

    override fun removeBlockEntity(x: Int, y: Int, z: Int) {
        TODO("Not yet implemented")
    }

    override fun blockEntitySequence(
        min: IntVector3,
        max: IntVector3,
        options: SequenceOptions
    ): VolumeSequence<ServerWorld, BlockEntity> {
        TODO("Not yet implemented")
    }

    override fun blockEntity(x: Int, y: Int, z: Int): BlockEntity? {
        TODO("Not yet implemented")
    }

    override fun block(x: Int, y: Int, z: Int): BlockState {
        TODO("Not yet implemented")
    }

    override fun fluid(x: Int, y: Int, z: Int): FluidState {
        TODO("Not yet implemented")
    }

    override fun highestYAt(x: Int, z: Int): Int {
        TODO("Not yet implemented")
    }

    override fun destroyBlock(pos: IntVector3, performDrops: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun setWeather(type: WeatherType, ticks: Int) {
        TODO("Not yet implemented")
    }

    override fun <T : Component> getComponent(type: KClass<T>): T? {
        TODO("Not yet implemented")
    }
}