package com.karbonpowered.api.world.chunk

import com.karbonpowered.api.Identifier
import com.karbonpowered.api.Karbon
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

object ChunkStates {
    /**
     * A [ProtoChunk] that is at this state means that it is being generated
     * with a "base" layer of terrain.
     * The chunk should not have any [Entity] instances or [BlockEntity]
     * instances and may have a valid [ProtoWorld] used for world generation.
     */
    val BASE = key(Identifier.minecraft("base"))

    /**
     * A [ProtoChunk] that is being "carved out" for general terrain features
     * that require things like "caves" or "canyons".
     */
    val CARVED = key(Identifier.minecraft("carved"))

    /**
     * A [ProtoChunk] state that is being populated by world generation,
     * usually provided by [Biome]s.
     */
    val DECORATED = key(Identifier.minecraft("decorated"))

    /**
     * Identifies a [ProtoChunk] that is considered empty. The method
     * [ProtoChunk.isEmpty] would return `true`. Identifies the
     * chunk has nothing contained within it, but can be used as a dummy chunk
     * in some regards for world generation.
     */
    val EMPTY = key(Identifier.minecraft("empty"))

    /**
     * A [ProtoChunk] state that is being used for entity spawning.
     * Generally requires that the neighboring chunks are adequately populated,
     * and requires that this chunk has proper lighting, for mob placement logic.
     */
    val ENTITIES_SPAWNED = key(Identifier.minecraft("entities_spawned"))

    /**
     * A [ProtoChunk] state that is "cleaning" up remnant objects of a
     * chunk in process of world generation. Generally, height maps are being
     * calculated at this point as entity spawning can affect block placement.
     */
    val FINALIZED = key(Identifier.minecraft("finalized"))

    /**
     * A [ProtoChunk] that has completed world generation tasks and can be
     * added to a level ready [World]. Likewise can be utilized during
     * chunk deserialization prior to a [Chunk] being fully added to a
     * [World] instance.
     */
    val GENERATED = key(Identifier.minecraft("generated"))

    /**
     * A [ProtoChunk] state that is being "carved" with liquid cave
     * features, such as underwater ravines, underwater caves, etc.
     */
    val LIQUID_CARVED = key(Identifier.minecraft("liquid_carved"))

    /**
     * A [ProtoChunk] state that has yet been processed with lighting in
     * respects to the [ProtoWorld] that contains it. This is the second
     * to last step in the world generation pipeline for a chunk to be marked
     * as ready for being added to a [World].
     */
    val LIT = key(Identifier.minecraft("lit"))

    /**
     * State for a [ProtoChunk] marking it being used by a world, and not
     * in the process of either world generation, or deserialization from storage.
     * Should have an instance of [Chunk] providing this state only, as
     * other [ProtoChunk]s would assuredly be invalid with this state.
     */
    val WORLD_READY = key(Identifier.minecraft("world_ready"))

    private fun key(identifier: Identifier): DefaultedRegistryReference<ChunkState> =
            RegistryKey(RegistryTypes.CHUNK_STATE, identifier).asDefaultedReference { Karbon.game.registries }
}