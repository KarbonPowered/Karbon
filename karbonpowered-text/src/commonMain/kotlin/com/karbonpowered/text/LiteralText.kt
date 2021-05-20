package com.karbonpowered.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface LiteralText : Text {
    val content: String
}

@Serializable
data class LiteralTextImpl(
    @SerialName("text")
    override val content: String
) : LiteralText {
    override fun toString(): String = Json.encodeToString(serializer(), this)
}

fun LiteralText(content: String): LiteralText = LiteralTextImpl(content)