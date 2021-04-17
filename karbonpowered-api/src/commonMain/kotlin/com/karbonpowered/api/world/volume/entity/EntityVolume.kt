package com.karbonpowered.api.world.volume.entity

import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.world.volume.MutableVolume
import com.karbonpowered.api.world.volume.Volume
import com.karbonpowered.api.world.volume.block.BlockVolume
import com.karbonpowered.api.world.volume.sequence.SequenceOptions
import com.karbonpowered.api.world.volume.sequence.VolumeSequence
import com.karbonpowered.common.UUID
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3

interface EntityVolume : Volume {
    val players: Collection<Player>

    fun entity(uuid: UUID): Entity<*>?

    fun nearestPlayer(
            x: Double,
            y: Double,
            z: Double,
            distance: Double,
            predicate: (Player) -> Boolean = { true }
    ): Player? {
        var nearest: Player? = null
        var closest = -1.0
        val distanceSquared = distance * distance
        players.forEach { player ->
            if (predicate(player)) {
                val dist = player.position.distanceSquared(x, y, z)
                if ((closest < 0 || dist < distanceSquared) && (closest == -1.0 || dist < distance)) {
                    nearest = player
                    closest = dist
                }
            }
        }
        return nearest
    }

    interface Sequence<E : Sequence<E>> : EntityVolume {
        fun entitySequence(min: IntVector3, max: IntVector3, options: SequenceOptions): VolumeSequence<E, Entity<*>>
    }

    interface Mutable<M : Mutable<M>> : Sequence<M>, MutableVolume, BlockVolume.Mutable<M> {
        fun <E : Entity<E>> createEntity(type: EntityType<E>, position: IntVector3): E =
                createEntity(type, position.toDouble())

        fun <E : Entity<E>> createEntity(type: EntityType<E>, position: DoubleVector3): E

        fun <E : Entity<E>> createEntityNaturally(type: EntityType<E>, position: IntVector3): E =
                createEntityNaturally(type, position.toDouble())

        fun <E : Entity<E>> createEntityNaturally(type: EntityType<E>, position: DoubleVector3): E

        fun spawnEntity(entity: Entity<*>): Boolean
        fun spawnEntities(entities: Iterable<Entity<*>>): Collection<Entity<*>>
    }
}