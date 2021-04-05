package com.karbonpowered.api.world.block

import com.karbonpowered.api.registry.builder
import com.karbonpowered.common.builder.Builder

/**
 * A flag of sorts that determines whether a block change will perform various
 * interactions, such as notifying neighboring blocks, performing block physics
 * on placement, etc.
 */
interface BlockChangeFlag {

    /**
     * Gets whether this flag defines that a block change should
     * notify neighboring blocks.
     *
     * @return True if this is set to notify neighboring blocks
     */
    val updateNeighbors: Boolean

    /**
     * Gets whether this flag defines that a block change should
     * update clients to perform a rendering update
     *
     * @return True if this is set to update clients
     */
    val notifyClients: Boolean

    /**
     * Gets whether this flag defines that a block change should
     * perform block physics checks or not. If not, no checks
     * are performed.
     *
     * @return True if this is set to perform block physics on placement
     */
    val performBlockPhysics: Boolean

    /**
     * Gets whether this flag will update observer blocks, different
     * from notifying neighbors in that neighbor notifications
     * can cause further block notification loops (like redstone),
     * whereas this focuses on [BlockTypes.OBSERVER] blocks
     * being told of updates.
     *
     * @return True if this is set to update observers.
     */
    val updateNeighboringShapes: Boolean

    /**
     * Gets whether this flag will queue lighting updates, different
     * blocks may affect lighting in locations, which can potentially
     * cause other blocks to perform side effects due to the light
     * changes (like mushrooms). It is not recommended to rely on this
     * particular flag for any changes that can cause client-side
     * lighting inconsistencies.
     *
     * @return True if this flag will update lighting
     */
    val updateLighting: Boolean

    /**
     * Gets whether this flag will notify pathfinders and navigators
     * for AI on entities and potentially other entities of a block
     * change. It may be helpful for mass block placement to bypass
     * a notification of pathfinders within an area.
     *
     * @return True if this flag will update pathing
     */
    val notifyPathfinding: Boolean

    /**
     * Gets whether this flag will allow blocks being destroyed as
     * a result of [updateNeighboringShapes] if the affected
     * blocks could be considered "destroyed" and drop items.
     *
     * @return True if this flag will allow neighboring blocks to perform drops
     */
    val neighborDropsAllowed: Boolean

    /**
     * Gets whether this flag is considering that blocks are being moved
     * in the world, much like how pistons will move blocks. This has some
     * effect on [BlockEntity] creation
     * reaction or drop delays. The behaviors are dependent on the block in
     * particular.
     *
     * @return True if the flag is considering blocks are moving
     */
    val movingBlocks: Boolean

    /**
     * Gets whether this block change is going to request the client to re-render
     * the block on chnge for the next client tick. This has particular usage
     * when performing complicated block changes with extra steps, such as
     * pistons performing a move.
     *
     * @return True if the blockchagne is requesting the client to re-render
     * the change on the next tick
     */
    val forceClientRerender: Boolean

    /**
     * Gets whether the block change is requested to have no render update on
     * the client. This is generally used only when [notifyClients]
     * is `true` as well as [forceClientRerender] is
     * `false`.
     *
     * @return True if the client is not to render te block change, usually
     * accompanied later by a manual update after some ticks
     */
    val ignoreRender: Boolean

    /**
     * Gets the equivalent [BlockChangeFlag] of this flag with all
     * other flags while having the desired `updateNeighbors`
     * as defined by the parameter.
     *
     * @param updateNeighbors Whether to update neighboring blocks
     * @return The relative flag with the desired update neighbors
     */
    fun withUpdateNeighbors(updateNeighbors: Boolean): BlockChangeFlag

    /**
     * Gets the equivalent [BlockChangeFlag] of this flag
     * with all other flags while having the desired `updateClients`
     * as defined by the parameter.
     *
     * @param updateClients Whether to update clients
     * @return The relative flag with the desired update clients
     */
    fun withNotifyClients(updateClients: Boolean): BlockChangeFlag

    /**
     * Gets the equivalent [BlockChangeFlag] of this flag
     * with all other flags while having the desired `performBlockPhysics`
     * as defined by the parameter.
     *
     * @param performBlockPhysics Whether to perform block physics
     * @return The relative flag with the desired block physics
     */
    fun withPhysics(performBlockPhysics: Boolean): BlockChangeFlag

    /**
     * Gets the equivalent [BlockChangeFlag] of this flag with all
     * other flags while having the desired `updateNeighboringShapes`
     * as defined by the parameter.
     *
     * @param notifyObservers Whether to update observer blocks
     * @return The relative flag with the desired notify observers
     */
    fun withNotifyObservers(notifyObservers: Boolean): BlockChangeFlag

    fun withLightingUpdates(lighting: Boolean): BlockChangeFlag

    fun withPathfindingUpdates(pathfindingUpdates: Boolean): BlockChangeFlag

    fun withNeighborDropsAllowed(dropsAllowed: Boolean): BlockChangeFlag

    fun withBlocksMoving(moving: Boolean): BlockChangeFlag

    fun withIgnoreRender(ignoreRender: Boolean): BlockChangeFlag

    fun withForcedReRender(forcedReRender: Boolean): BlockChangeFlag

    /**
     * Gets the inverted [BlockChangeFlag] of this flag.
     * Normally, this may cancel out certain interactions, such
     * as physics, neighbor notifications, or even observer
     * notifications. In certain circumstances, some flags may
     * even require clients to rejoin the world or restart their
     * connections to the server.
     *
     * @return The inverted flag
     */
    fun inverse(): BlockChangeFlag

    /**
     * Gets the equivalent [BlockChangeFlag] of this flag
     * with the `true`s set for this flag and the provided
     * `flag`, such that only if both flags have the same
     * `true` flags set will persist.
     *
     *
     * For example, if this flag has [updateNeighboringShapes]
     * and the incoming flag has [updateNeighboringShapes] returning
     * `true`, the resulting flag will have
     * [updateNeighboringShapes] return `true` as well. The
     * inverse is also true. If either has differing flags for any
     * of the above methods, the resulting flag will have a
     * `false` value.
     *
     * @param flag The incoming flag to and with this flag
     * @return The resulting flag with matched values
     */
    fun andFlag(flag: BlockChangeFlag): BlockChangeFlag

    /**
     * Gets the equivalent [BlockChangeFlag] of this flag
     * with the `true`s set for this flag and the provided
     * `flag`, such that only if both flags have the same
     * `true` flags set will persist.
     *
     *
     * For example, if this flag has [updateNeighboringShapes]
     * and the incoming flag has [updateNeighboringShapes] returning
     * `true`, the resulting flag will have
     * [updateNeighboringShapes] return `true` as well. The
     * inverse is also true. If either has differing flags for any
     * of the above methods, the resulting flag will have a
     * `false` value.
     *
     * @param flag The incoming flag to and with this flag
     * @return The resulting flag with matched values
     */
    fun andNotFlag(flag: BlockChangeFlag): BlockChangeFlag

    companion object {
        fun builder(): Builder = builder<Builder>()
    }

    interface Factory {
        /**
         * Provides a [BlockChangeFlag] where all flags are `false`.
         *
         *  * [BlockChangeFlag.updateNeighbors] is `false`
         *  * [BlockChangeFlag.notifyClients] is `false`
         *  * [BlockChangeFlag.updateNeighboringShapes] is `false`
         *  * [BlockChangeFlag.updateLighting] is `false`
         *  * [BlockChangeFlag.performBlockPhysics] is `false`
         *  * [BlockChangeFlag.notifyPathfinding] is `false`
         *
         * @return The all false change flag
         */
        fun none(): BlockChangeFlag
    }

    interface Builder : com.karbonpowered.common.builder.Builder<BlockChangeFlag, Builder> {
        var updateNeighbors: Boolean
        var notifyClients: Boolean
        var performBlockPhysics: Boolean
        var updateNeighboringShapes: Boolean
        var updateLighting: Boolean
        var notifyPathfinding: Boolean
        var neighborDropsAllowed: Boolean
        var movingBlocks: Boolean
        var forceClientRerender: Boolean
        var ignoreRender: Boolean
    }
}

inline operator fun BlockChangeFlag.Companion.invoke(builder: BlockChangeFlag.Builder.() -> Unit): BlockChangeFlag = builder().apply(builder).build()