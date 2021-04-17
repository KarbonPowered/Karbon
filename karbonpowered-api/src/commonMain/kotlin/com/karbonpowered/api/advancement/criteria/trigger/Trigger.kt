package com.karbonpowered.api.advancement.criteria.trigger

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.ResourceKeyed
import com.karbonpowered.api.entity.living.player.server.ServerPlayer
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

import kotlin.reflect.KClass

interface Trigger<C : FilteredTriggerConfiguration> : DefaultedRegistryValue, ResourceKeyed {
    val configurationType: KClass<*>

    fun trigger()
    fun trigger(player: ServerPlayer)
    fun trigger(vararg player: ServerPlayer)
    fun trigger(player: Iterable<ServerPlayer>)
}

object Triggers {
    val BAD_OMEN by key(ResourceKey.minecraft("voluntary_exile"))
    val BEE_NEST_DESTROYED by key(ResourceKey.minecraft("bee_nest_destroyed"))
    val BRED_ANIMALS by key(ResourceKey.minecraft("bred_animals"))
    val BREWED_POTION by key(ResourceKey.minecraft("brewed_potion"))
    val CHANGED_DIMENSION by key(ResourceKey.minecraft("changed_dimension"))
    val CHANNELED_LIGHTNING by key(ResourceKey.minecraft("channeled_lightning"))
    val CONSTRUCT_BEACON by key(ResourceKey.minecraft("construct_beacon"))
    val CONSUME_ITEM by key(ResourceKey.minecraft("consume_item"))
    val CURED_ZOMBIE_VILLAGER by key(ResourceKey.minecraft("cured_zombie_villager"))
    val EFFECTS_CHANGED by key(ResourceKey.minecraft("effects_changed"))
    val ENCHANTED_ITEM by key(ResourceKey.minecraft("enchanted_item"))
    val ENTER_BLOCK by key(ResourceKey.minecraft("enter_block"))
    val ENTITY_HURT_PLAYER by key(ResourceKey.minecraft("entity_hurt_player"))
    val ENTITY_KILLED_PLAYER by key(ResourceKey.minecraft("entity_killed_player"))
    val FILLED_BUCKET by key(ResourceKey.minecraft("filled_bucket"))
    val FISHING_ROD_HOOKED by key(ResourceKey.minecraft("fishing_rod_hooked"))
    val GENERATE_LOOT by key(ResourceKey.minecraft("player_generates_container_loot"))
    val HONEY_BLOCK_SIDE by key(ResourceKey.minecraft("slide_down_block"))
    val IMPOSSIBLE by key(ResourceKey.minecraft("impossible"))
    val INVENTORY_CHANGED by key(ResourceKey.minecraft("inventory_changed"))
    val ITEM_DURABILITY_CHANGED by key(ResourceKey.minecraft("item_durability_changed"))
    val ITEM_PICKED_UP_BY_ENTITY by key(ResourceKey.minecraft("thrown_item_picked_up_by_entity"))
    val ITEM_USED_ON_BLOCK by key(ResourceKey.minecraft("item_used_on_block"))
    val KILLED_BY_CROSSBOW by key(ResourceKey.minecraft("killed_by_crossbow"))
    val LEVITATION by key(ResourceKey.minecraft("levitation"))
    val LOCATION by key(ResourceKey.minecraft("location"))
    val NETHER_TRAVEL by key(ResourceKey.minecraft("nether_travel"))
    val PLACED_BLOCK by key(ResourceKey.minecraft("placed_block"))
    val PLAYER_HURT_ENTITY by key(ResourceKey.minecraft("player_hurt_entity"))
    val PLAYER_INTERACTED_WITH_ENTITY by key(ResourceKey.minecraft("player_interacted_with_entity"))
    val PLAYER_KILLED_ENTITY by key(ResourceKey.minecraft("player_killed_entity"))
    val RAID_WIN by key(ResourceKey.minecraft("hero_of_the_village"))
    val RECIPE_UNLOCKED by key(ResourceKey.minecraft("recipe_unlocked"))
    val SHOT_CROSSBOW by key(ResourceKey.minecraft("shot_crossbow"))
    val SLEPT_IN_BED by key(ResourceKey.minecraft("slept_in_bed"))
    val SUMMONED_ENTITY by key(ResourceKey.minecraft("summoned_entity"))
    val TAME_ANIMAL by key(ResourceKey.minecraft("tame_animal"))
    val TARGET_BLOCK_HIT by key(ResourceKey.minecraft("target_hit"))
    val TICK by key(ResourceKey.minecraft("tick"))
    val USED_ENDER_EYE by key(ResourceKey.minecraft("used_ender_eye"))
    val USED_TOTEM by key(ResourceKey.minecraft("used_totem"))
    val VILLAGER_TRADE by key(ResourceKey.minecraft("villager_trade"))

    private fun key(location: ResourceKey): DefaultedRegistryReference<Trigger<*>> =
            RegistryKey(RegistryTypes.TRIGGER, location).asDefaultedReference { Karbon.game.registries }
}