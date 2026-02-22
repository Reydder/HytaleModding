package com.reydder.shop.systems

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.entities.NPCEntity

class EnemyDamageSystem: DamageEventSystem() {
    override fun handle(
        index: Int,
        archetypeChunk: ArchetypeChunk<EntityStore?>,
        store: Store<EntityStore?>,
        commandBuffer: CommandBuffer<EntityStore?>,
        damage: Damage
    ) {
        if (damage.amount <= 0F) {
            HytaleLogger.getLogger().atInfo().log("No damage applied")
            return
        }

        val source = damage.source
        if (source is Damage.EntitySource) {
            val sourceRef = source.ref
            val player = store.getComponent(sourceRef, Player.getComponentType())
            val playerRef = store.getComponent(sourceRef, PlayerRef.getComponentType())
            val entityStatMap = store.getComponent(sourceRef, EntityStatMap.getComponentType())

            if (player == null || entityStatMap == null || playerRef == null) {
                HytaleLogger.getLogger().atInfo().log("Damage source is not a player")
                return
            }

            val pointsIndex = EntityStatType.getAssetMap()?.getIndex("Points")
            if (pointsIndex == null) {
                HytaleLogger.getLogger().atInfo().log("No Points stat found")
                return
            }
            entityStatMap.addStatValue(pointsIndex, 10.0F)
        } else {
            HytaleLogger.getLogger().atInfo().log("Damage source is not an entity")
        }
    }

    override fun getQuery(): Query<EntityStore?> {
        return Query.and(NPCEntity.getComponentType(), UUIDComponent.getComponentType())
    }
}