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
import it.unimi.dsi.fastutil.objects.Object2FloatMap
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap
import java.util.function.Supplier
import java.util.logging.Level

class ReloadConditionInteraction: SimpleInstantInteraction() {

    companion object {
        @JvmStatic
        val CODEC: BuilderCodec<ReloadConditionInteraction> = BuilderCodec.builder(
            ReloadConditionInteraction::class.java, { ReloadConditionInteraction()}, SimpleInstantInteraction.CODEC)
            .append(
                KeyedCodec(
                    "StatToCheck",
                    Object2FloatMapCodec(Codec.STRING,
                        {Object2FloatOpenHashMap()}),
                    true),
                {interaction, map -> interaction.statsToCheck = map},
                {interaction -> interaction.statsToCheck}
            )
            .addValidator(Validators.nonNull())
            .addValidator(Validators.nonEmptyMap())
            .addValidator(EntityStatType.VALIDATOR_CACHE.getMapKeyValidator())
            .add()
            .build()

    }

    private var statsToCheck: Object2FloatMap<String> = Object2FloatOpenHashMap()

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

        for (entry in statsToCheck) {
            val statIndex = EntityStatType.getAssetMap().getIndex(entry.key)
            val currentStatValue = entityStatMap.get(statIndex)

            if (currentStatValue == null) {
                interactionContext.state.state = InteractionState.Failed
                return
            }

            if (currentStatValue.get() == currentStatValue.max) {
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
}