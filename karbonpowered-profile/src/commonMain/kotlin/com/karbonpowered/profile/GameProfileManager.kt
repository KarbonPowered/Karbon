package com.karbonpowered.profile

interface GameProfileManager : GameProfileProvider {
    val cache: GameProfileCache
    val uncached: GameProfileProvider
}