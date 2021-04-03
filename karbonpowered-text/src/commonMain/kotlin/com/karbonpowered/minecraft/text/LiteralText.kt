package com.karbonpowered.minecraft.text

class LiteralText(val value: String) : Text() {
    override fun toString(): String = "{\"text\":\"$value\"}"
}