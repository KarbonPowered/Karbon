package com.karbonpowered.minecraft.text

abstract class Text : TextRepresentable {
    override fun toText(): Text = this
}