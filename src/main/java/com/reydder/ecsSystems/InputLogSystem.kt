package com.reydder.ecsSystems

import com.hypixel.hytale.assetstore.AssetRegistry
import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.packets.player.ReticleEvent
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.reydder.posion.PoisonComponent
import java.util.logging.Level

class InputLogSystem(val componentType: ComponentType<EntityStore?, PoisonComponent>): EntityEventSystem<EntityStore, Damage>(Damage::class.java) {

    companion object {
        private val EVENT_ON_HIT_TAG_INDEX: Int = AssetRegistry.getOrCreateTagIndex("OnHit")
        private val EVENT_ON_KILL_TAG_INDEX: Int = AssetRegistry.getOrCreateTagIndex("OnKill")
        private val ON_HIT: ReticleEvent = ReticleEvent(EVENT_ON_HIT_TAG_INDEX)
        private val ON_KILL: ReticleEvent = ReticleEvent(EVENT_ON_KILL_TAG_INDEX)
    }

    override fun getQuery(): Query<EntityStore?> {
        return Query.any()
    }

    override fun handle(
        index: Int,
        archetypeChunk: ArchetypeChunk<EntityStore?>,
        store: Store<EntityStore?>,
        commandbuffer: CommandBuffer<EntityStore?>,
        event: Damage
    ) {
        val entity = archetypeChunk.getReferenceTo(index)

        HytaleLogger.getLogger().at(Level.INFO).log("${event.source}")

        val projectSource = event.source as? Damage.ProjectileSource
        projectSource?.let { projectileSource ->
            HytaleLogger.getLogger().at(Level.INFO).log("${event.source}")

            val poisonComponent = store.getComponent(entity, componentType)
            if (poisonComponent == null) {
                commandbuffer.addComponent(entity, componentType)
            }
        }


        /*NPCEntity.getComponentType()?.let {
            val npcEntity = store.getComponent(entity, it)
            HytaleLogger.getLogger().at(Level.INFO).log("Player damaged: ${npcEntity?.role}")

        }





        val player = store.getComponent(entity, Player.getComponentType())

        player?.let {
            HytaleLogger.getLogger().at(Level.INFO).log("Player damaged: ${player.playerRef.username}")
        }

        //val itemComponent = store.getComponent(entity, ItemComponent.getComponentType())



        //commandbuffer.addComponent(entity, componentType, PoisonComponent())
        HytaleLogger.getLogger().at(Level.INFO).log("Enemy poisoned")

        //HytaleLogger.getLogger().at(Level.INFO).log("Dropping item ${itemComponent?.itemStack?.itemId}")*/

    }
}