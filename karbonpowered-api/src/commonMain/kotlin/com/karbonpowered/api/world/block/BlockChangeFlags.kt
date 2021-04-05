package com.karbonpowered.api.world.block

import com.karbonpowered.api.registry.factory

object BlockChangeFlags {

    /**
     * All the available flags are applied through the OR operator.
     */
    val ALL = factory<BlockChangeFlag.Factory>().none().inverse()

    /**
     * The default flags for a placement event, such as a player placing a block,
     * another entity placing a block, etc.
     *
     * Note: While players are normally placing blocks with this flag, there's
     * other circumstances of placing a block "as" a [Player]
     * that are covered outside the purview of this default placement flag.
     */
    val DEFAULT_PLACEMENT = BlockChangeFlag {
        notifyClients = true
        updateNeighbors = true
        performBlockPhysics = true
        notifyClients = true
        neighborDropsAllowed = true
        updateLighting = true
        notifyPathfinding = true
    }

    /**
     * No flags are set, triggers nothing, the following flags are as such:
     *
     *  * [BlockChangeFlag.notifyClients] is `false`
     *  * [BlockChangeFlag.updateNeighbors] ()} is `false`
     *  * [BlockChangeFlag.forceClientRerender] is `false`
     *  * [BlockChangeFlag.ignoreRender] is `false`
     *  * [BlockChangeFlag.movingBlocks] is `false`
     *  * [BlockChangeFlag.updateLighting] is `false`
     *  * [BlockChangeFlag.updateNeighboringShapes] is `false`
     *  * [BlockChangeFlag.performBlockPhysics] is `false`
     *  * [BlockChangeFlag.notifyPathfinding] is `false`
     *
     */
    val NONE = factory<BlockChangeFlag.Factory>().none()

    /**
     * Sends block changes to clients but does not trigger block updates or
     * other neighboring notification updates. It does enable lighting updates,
     * usually much more preferred to having a "minimal"
     */
    val NOTIFY_CLIENTS = BlockChangeFlag {
        updateLighting = true
        notifyClients = true
    }
}