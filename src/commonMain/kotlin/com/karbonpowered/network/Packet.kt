package com.karbonpowered.network

interface Packet {
    override fun toString(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}