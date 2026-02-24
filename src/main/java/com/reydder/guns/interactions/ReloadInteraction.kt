package com.reydder.guns.interactions

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.codecs.map.Object2FloatMapCodec
import com.hypixel.hytale.codec.validation.Validators
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
import it.unimi.dsi.fastutil.objects.Object2FloatMap
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap
import java.util.logging.Level

class ReloadInteraction: SimpleInstantInteraction() {

    companion object {
        @JvmStatic
        public val CODEC = BuilderCodec<ReloadInteraction>.builder(ReloadInteraction::class.java,  {ReloadInteraction()},
            SimpleInteraction.CODEC)
            .append(
                KeyedCodec(
                    "StatToReload",
                    Object2FloatMapCodec(Codec.STRING,
                        {Object2FloatOpenHashMap()}),
                    true),
                {interaction, map -> interaction.statsToReload = map},
                {interaction -> interaction.statsToReload}
            )
            .addValidator(Validators.nonNull())
            .addValidator(Validators.nonEmptyMap())
            .addValidator(EntityStatType.VALIDATOR_CACHE.getMapKeyValidator())
            .add()
            .build()
    }

    private var statsToReload: Object2FloatMap<String> =  Object2FloatOpenHashMap()

    override fun firstRun(
        interactionType: InteractionType,
        interactionContext: InteractionContext,
        cooldownHandler: CooldownHandler
    ) {
        val ref = interactionContext.entity
        val store = ref.store

        val player = store.getComponent(ref, Player.getComponentType())
        val entityStatMap = store.getComponent(ref, EntityStatMap.getComponentType())

        if (player == null || entityStatMap == null) {
            interactionContext.state.state = InteractionState.Failed
            return
        }

        for (entry in statsToReload) {
            val statIndex = EntityStatType.getAssetMap().getIndex(entry.key)
            val currentStatValue = entityStatMap.get(statIndex)

            if (currentStatValue == null) {
                interactionContext.state.state = InteractionState.Failed
                return
            }

            var combinedHotbarFirst = player.inventory.combinedHotbarFirst

            var pendingAmmoReload = currentStatValue.max - currentStatValue.get()
            var reloadedAmmo = 0

            if (pendingAmmoReload == 0F) {
                interactionContext.state.state = InteractionState.Failed
                return
            }

            for (i in 0..< combinedHotbarFirst.capacity) {
                val itemStack = combinedHotbarFirst.getItemStack(i.toShort()) ?: continue
                if (itemStack.itemId == "Weapon_Bullet") {
                    val quantityRemainingAdjustment = Math.min(itemStack.quantity, pendingAmmoReload.toInt())
                    combinedHotbarFirst.removeItemStackFromSlot(i.toShort(), quantityRemainingAdjustment)
                    pendingAmmoReload -= quantityRemainingAdjustment
                    reloadedAmmo += quantityRemainingAdjustment
                }
            }

            // Update EntityStatMap
            entityStatMap.addStatValue(statIndex, reloadedAmmo.toFloat())
        }
    }


}