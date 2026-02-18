package com.reydder.spawn.systems

import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.RemoveReason
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.core.util.EventTitleUtil
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.reydder.spawn.GameManager
import kotlin.collections.component1
import kotlin.collections.component2

class OnPlayerDeadSystem: DeathSystems.OnDeathSystem() {
    override fun onComponentAdded(
        ref: Ref<EntityStore?>,
        deathComponent: DeathComponent,
        store: Store<EntityStore?>,
        commandBuffer: CommandBuffer<EntityStore?>
    ) {
        for ((game, gameConfig) in GameManager.get().activeGames) {
            val world = store.externalData.world
            if (game != world.name) continue

            GameManager.get().reset(game)

            store.forEachEntityParallel { index, archetypeChunk, commandBuffer ->
                val npcComponent = archetypeChunk.getComponent<NPCEntity>(index, NPCEntity.getComponentType()!!)
                val name = npcComponent?.role?.roleName

                if (name?.contains("Zombie") == true) {
                    val ref = archetypeChunk?.getReferenceTo(index)
                    ref?.let {
                        world.execute {
                            store.removeEntity(it, RemoveReason.REMOVE)
                        }
                    }
                }
            }

            EventTitleUtil.showEventTitleToWorld(
                Message.raw("Game over"),
                Message.raw(""),
                true,
                null,
                3.0F,
                1.0F,
                1.0F,
                store
            )
        }
    }

    override fun getQuery(): Query<EntityStore?> {
        return Player.getComponentType()
    }
}