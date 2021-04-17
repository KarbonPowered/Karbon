package com.karbonpowered.text

interface LiteralText : Text {
    val content: String
}

data class KarbonLiteralText(override val content: String) : LiteralText {
    override fun toString(): String = content.replace("\"", "\\\"")
}

fun LiteralText(content: String): LiteralText = KarbonLiteralText(content)