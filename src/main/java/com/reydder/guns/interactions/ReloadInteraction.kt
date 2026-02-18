package com.reydder.guns.interactions

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.protocol.InteractionState
import com.hypixel.hytale.protocol.InteractionType
import com.hypixel.hytale.server.core.entity.InteractionContext
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction

class ReloadInteraction: SimpleInstantInteraction() {

    companion object {
        @JvmStatic
        public val CODEC = BuilderCodec<ReloadInteraction>.builder(ReloadInteraction::class.java,  {ReloadInteraction()},
            SimpleInteraction.CODEC).build()
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

        var combinedHotbarFirst = player.inventory.combinedHotbarFirst

        var pendingAmmoReload = 30 - currentRifleAmmo
        var reloadedAmmo = 0

        if (pendingAmmoReload == 0) {
            interactionContext.state.state = InteractionState.Failed
            return
        }

        for (i in 0..< combinedHotbarFirst.capacity) {
            val itemStack = combinedHotbarFirst.getItemStack(i.toShort()) ?: continue
            if (itemStack.itemId == "Weapon_Bullet") {
                val quantityRemainingAdjustment = Math.min(itemStack.quantity, pendingAmmoReload)
                combinedHotbarFirst.removeItemStackFromSlot(i.toShort(), quantityRemainingAdjustment)
                pendingAmmoReload -= quantityRemainingAdjustment
                reloadedAmmo += quantityRemainingAdjustment

            }
        }

        // Update EntityStatMap
        entityStatMap.addStatValue(statIndex, reloadedAmmo.toFloat())
    }


}