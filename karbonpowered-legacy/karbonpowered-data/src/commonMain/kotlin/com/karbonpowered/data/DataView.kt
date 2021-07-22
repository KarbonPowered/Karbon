package com.karbonpowered.data

interface DataView {
    val container: DataContainer
    val currentPath: DataQuery
    val name: String
    val parent: DataView?
    val safetyMode: SafetyMode

    fun keys(deep: Boolean): Set<DataQuery>
    fun values(deep: Boolean): Map<DataQuery, Any>

    operator fun contains(path: DataQuery): Boolean
    fun contains(path: DataQuery, vararg paths: DataQuery): Boolean

    operator fun get(path: DataQuery): Any?
    operator fun set(path: DataQuery, value: Any): DataView
    fun remove(path: DataQuery): DataView
    fun createView(path: DataQuery): DataView
    fun createView(path: DataQuery, map: Map<*, *>): DataView
    fun getView(path: DataQuery): DataView?

    fun copy(): DataContainer
    fun copy(safetyMode: SafetyMode): DataContainer
    fun isEmpty(): Boolean

    /**
     * The safety mode of the container.
     */
    enum class SafetyMode {
        /**
         * All data added to the container will be cloned for safety.
         */
        ALL_DATA_CLONED,

        /**
         * All data added to the container will be cloned for safety.
         */
        CLONED_ON_SET,

        /**
         * No data added to the container will be cloned, useful for situations
         * with a large amount of data where the cloning would be too costly.
         */
        NO_DATA_CLONED
    }
}