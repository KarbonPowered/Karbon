package com.karbonpowered.api

data class Identifier(
    val namespace: String,
    val value: String
) {
    override fun toString(): String = "$namespace:$value"
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