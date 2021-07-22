package com.karbonpowered.text

interface Text : TextRepresentable {
    override fun toText(): Text = this
}