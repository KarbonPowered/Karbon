package com.karbonpowered.data

interface DataQuery : Iterable<String> {
    val parts: List<String>
    val queryParts: List<DataQuery>

    interface Factory {
        fun create(parts: Iterable<String>): DataQuery
    }

    companion object {
        lateinit var factory: Factory

        fun of(separator: Char, path: String): DataQuery = factory.create(path.split(separator))
        fun of(vararg parts: String): DataQuery = factory.create(parts.toList())
        fun of(parts: Iterable<String>): DataQuery = factory.create(parts)
    }
}

fun DataQuery(separator: Char, path: String): DataQuery = DataQuery.of(separator, path)
fun DataQuery(vararg parts: String): DataQuery = DataQuery.of(*parts)
fun DataQuery(parts: Iterable<String>): DataQuery = DataQuery.of(parts)

data class KarbonDataQuery(
        override val parts: List<String>
) : DataQuery {
    override val queryParts: List<DataQuery>
        get() = TODO("Not yet implemented")

    override fun iterator(): Iterator<String> {
        TODO("Not yet implemented")
    }
}