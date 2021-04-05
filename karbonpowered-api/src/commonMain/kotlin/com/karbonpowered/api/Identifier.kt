package com.karbonpowered.api

data class Identifier(
        val namespace: String,
        val value: String
) {
    override fun toString(): String = "$namespace:$value"

    companion object {
        val BRIGADIER_NAMESPACE = "brigadier"
        val MINECRAFT_NAMESPACE = "minecraft"
        val KARBON_NAMESPACE = "karbon"

        fun brigadier(value: String): Identifier = Identifier(BRIGADIER_NAMESPACE, value)
        fun minecraft(value: String): Identifier = Identifier(MINECRAFT_NAMESPACE, value)
        fun karbon(value: String): Identifier = Identifier(KARBON_NAMESPACE, value)
    }
}

private val chars = "0123456789abcdefghijklmnopqrstuvwxyz-_".toCharArray()

fun Identifier(string: String): Identifier {
    var separators = 0
    string.forEachIndexed { index, c ->
        require(c in chars || c != '-' || separators++ == 1) {
            "Illegal char '$c' at index $index"
        }
    }
    val split = string.split(":")
    return Identifier(split[0], split[1])
}