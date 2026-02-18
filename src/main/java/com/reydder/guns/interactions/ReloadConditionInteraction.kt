package com.reydder.guns.interactions

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.InteractionState
import com.hypixel.hytale.protocol.InteractionType
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction
import java.util.logging.Level

class ReloadConditionInteraction: SimpleInstantInteraction() {

    companion object {
        @JvmStatic
        public val CODEC = BuilderCodec<ReloadInteraction>.builder(
            ReloadConditionInteraction::class.java, { ReloadConditionInteraction() },
            SimpleInteraction.CODEC
        ).build()
    }

    override fun firstRun(
        interactionType: InteractionType,
        interactionContext: InteractionContext,
        cooldownHandler: CooldownHandler
    ) {
        val ref = interactionContext.entity
        val store = ref.store

        val player = store.getComponent(ref, Player.getComponentType())
        val entityStatMap = store.getComponent(ref, EntityStatMap.getComponentType())
        val statIndex = EntityStatType.getAssetMap().getIndex("Rifle_Ammo")
        val currentRifleAmmo = entityStatMap?.get(statIndex)?.get()?.toInt()

        if (player == null || entityStatMap == null || currentRifleAmmo == null) {
            interactionContext.state.state = InteractionState.Failed
            return
        }

        if (currentRifleAmmo == 30) {
            HytaleLogger.getLogger().at(Level.INFO).log("Full charged")
            interactionContext.state.state = InteractionState.Failed
            return
        }

        val combinedHotbarFirst = player.inventory.combinedHotbarFirst

        var ammoAvailable = 0

        for (i in 0..< combinedHotbarFirst.capacity) {
            val itemStack = combinedHotbarFirst.getItemStack(i.toShort()) ?: continue
            if (itemStack.itemId == "Weapon_Bullet") {
                ammoAvailable += itemStack.quantity
            }
        }

        if (ammoAvailable == 0) {
            HytaleLogger.getLogger().at(Level.INFO).log("No ammo available")
            interactionContext.state.state = InteractionState.Failed
            return
        }

        HytaleLogger.getLogger().at(Level.INFO).log("Ammo available")
    }
}