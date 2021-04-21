package com.karbonpowered.data

interface DataContainer : DataView {
    override fun set(path: DataQuery, value: Any): DataContainer
    override fun remove(path: DataQuery): DataContainer

    interface Factory {
        fun create(safetyMode: DataView.SafetyMode = DataView.SafetyMode.ALL_DATA_CLONED): DataContainer
    }

    companion object {
        lateinit var factory: Factory

        fun create(safetyMode: DataView.SafetyMode = DataView.SafetyMode.ALL_DATA_CLONED) = factory.create(safetyMode)
    }
}

fun DataContainer(
    safetyMode: DataView.SafetyMode = DataView.SafetyMode.ALL_DATA_CLONED,
    builder: DataContainer.() -> Unit = {}
) =
    DataContainer.factory.create(safetyMode).apply(builder)