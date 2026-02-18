package com.reydder.spawn.systems

import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.core.util.EventTitleUtil
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.reydder.spawn.GameManager

class ZombieDeadSystem: DeathSystems.OnDeathSystem() {
    override fun onComponentAdded(
        ref: Ref<EntityStore?>,
        deathComonent: DeathComponent,
        store: Store<EntityStore?>,
        p3: CommandBuffer<EntityStore?>
    ) {
        for ((game, gameConfig) in GameManager.get().activeGames) {
            if (game != store.externalData.world.name) continue

            val damageSource = deathComonent.deathInfo?.source

            if (damageSource is Damage.EntitySource) {
                GameManager.get().zombieKilled()

                val playerRef = store.getComponent(damageSource.ref, PlayerRef.getComponentType())
                HytaleLogger.getLogger().atInfo().log("Zombie killed by ${playerRef?.username}")
                HytaleLogger.getLogger().atInfo()
                    .log("Zombies killed ${GameManager.Companion.get().zombiesKilled.get()}")

                if (GameManager.get().zombiesKilled.get() == GameManager.get().zombies) {
                    GameManager.get().nextRound(game)
                }
            }
        }
    }

    override fun getQuery(): Query<EntityStore?>? {
        return Query.and(NPCEntity.getComponentType(), UUIDComponent.getComponentType())
    }


}