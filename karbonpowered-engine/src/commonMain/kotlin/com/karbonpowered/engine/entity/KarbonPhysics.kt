package com.karbonpowered.engine.entity

import com.karbonpowered.engine.world.KarbonRegion
import com.karbonpowered.engine.world.KarbonWorldManager
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Position
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.discrete.TransformProvider
import com.karbonpowered.engine.world.reference.WorldReference
import com.karbonpowered.math.imaginary.FloatQuaternion
import com.karbonpowered.math.vector.FloatVector3
import kotlinx.atomicfu.atomic

class KarbonPhysics(
    val entity: KarbonEntity,
    transform: Transform? = null
) : TransformProvider {
    private val live = atomic(transform ?: Transform.INVALID)
    val snapshot = atomic(transform ?: Transform.INVALID)

    override var transform: Transform
        get() = live.value
        set(value) {
            var oldTransform: Transform
            var newTransform: Transform
            do {
                oldTransform = live.value
                newTransform = value
            } while (!live.compareAndSet(oldTransform, newTransform))
            sync()
        }

    var position: Position
        get() = live.value.position
        set(value) {
            var oldTransform: Transform
            var newTransform: Transform
            do {
                oldTransform = live.value
                newTransform = oldTransform.copy(position = value)
            } while (!live.compareAndSet(oldTransform, newTransform))
            sync()
        }

    var rotation: FloatQuaternion
        get() = live.value.rotation
        set(value) {
            var oldTransform: Transform
            var newTransform: Transform
            do {
                oldTransform = live.value
                newTransform = oldTransform.copy(rotation = value)
            } while (!live.compareAndSet(oldTransform, newTransform))
            sync()
        }

    var scale: FloatVector3
        get() = live.value.scale
        set(value) {
            var oldTransform: Transform
            var newTransform: Transform
            do {
                oldTransform = live.value
                newTransform = oldTransform.copy(scale = value)
            } while (!live.compareAndSet(oldTransform, newTransform))
            sync()
        }

    val world: WorldReference
        get() = position.world

    val isPositionDirty get() = snapshot.value.position != live.value.position
    val isRotationDirty get() = snapshot.value.rotation != live.value.rotation
    val isScaleDirty get() = snapshot.value.scale != live.value.scale
    val isWorldDirty get() = snapshot.value.position.world != live.value.position.world

    fun isRegionDirty(worldManager: KarbonWorldManager) =
        snapshot.value.position.region(worldManager, LoadOption.NO_LOAD) !=
                live.value.position.region(worldManager, LoadOption.NO_LOAD)

    fun crossInto(regionSnapshot: KarbonRegion, regionLive: KarbonRegion) {
        // TODO
    }

    fun copySnapshot() {
        snapshot.value = live.value
    }

    private fun sync() {

    }

    override fun toString(): String {
        return "KarbonPhysics(entity=$entity, live=$live, snapshot=$snapshot)"
    }

    fun onPrePhysicsTick() {

    }

    fun onPostPhysicsTick() {

    }
}