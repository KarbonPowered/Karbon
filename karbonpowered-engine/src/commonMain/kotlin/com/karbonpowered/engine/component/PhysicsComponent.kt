package com.karbonpowered.engine.component

import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.snapshot.Snapshotable
import com.karbonpowered.engine.util.Transform
import com.karbonpowered.math.imaginary.FloatQuaternion
import com.karbonpowered.math.vector.FloatVector3

/**
 * Component that gives the owner the characteristics to be a part of a Scene.
 * A Scene consists of [Transform]s which represent the snapshot state, the live state, and the rendering state.
 * This component can be used to manipulate the object within the scene.
 */
class PhysicsComponent(entity: KarbonEntity) : EntityComponent(entity), Snapshotable {
    private val snapshot = Transform()
    val transformLive = Transform()

    /**
     * Gets the [Transform] this [KarbonEntity] had within the last game tick.
     *
     * The Transform is stable, it is completely impossible for it to be updated.
     *
     * @return The Transform as of the last game tick.
     */
    var transform: Transform
        get() = snapshot.copy()
        set(value) {
            setTransform(value, true)
        }

    var position: FloatVector3
        get() = snapshot.position
        set(value) {
            transformLive.position = value
            sync()
        }

    var scale: FloatVector3
        get() = snapshot.scale
        set(value) {
            transformLive.scale = value
            sync()
        }

    val isTransformDirty: Boolean = snapshot != transformLive
    val isPositionDirty: Boolean = snapshot.position != transformLive.position
    val isScaleDirty: Boolean = snapshot.scale != transformLive.scale

    fun translate(offset: FloatVector3) = apply {
        transformLive.translate(offset)
    }

    fun rotate(offset: FloatQuaternion) = apply {
        transformLive.rotate(offset)
        sync()
    }

    fun scale(offset: FloatVector3) = apply {
        transformLive.scale(offset)
        sync()
    }

    /**
     * Sets the [Transform] for this [KarbonEntity].
     *
     * This function sets the live state of the entity's transform, not the snapshot state.
     * As such, it's advised to set the transform lastly else retrieving
     * the transform afterwards within the same tick will not return expected values
     * (due to potential other plugin changes as well as [transform])
     * returning snapshot state.
     *
     * @param transform The new live transform state of this entity.
     * @param sync To sync the changes with the client
     */
    fun setTransform(transform: Transform, sync: Boolean) = apply {
        transformLive.set(transform)
        if (sync) {
            sync()
        }
    }

    override fun copySnapshot() {
        snapshot.set(transformLive)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PhysicsComponent

        if (snapshot != other.snapshot) return false
        if (transformLive != other.transformLive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = snapshot.hashCode()
        result = 31 * result + transformLive.hashCode()
        return result
    }

    override fun toString(): String = "PhysicsComponent(snapshot=$snapshot, live=$transformLive)"

    private fun sync() {
        if (entity is KarbonPlayer) {
            entity.network.forceSync()
        }
    }
}