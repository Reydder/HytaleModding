package com.reydder.spawn

import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.core.util.EventTitleUtil
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.hypixel.hytale.server.npc.systems.NPCSystems
import java.util.UUID
import javax.annotation.Nonnull

class ZombieDeadSystem: DeathSystems.OnDeathSystem() {
    override fun onComponentAdded(
        ref: Ref<EntityStore?>,
        deathComonent: DeathComponent,
        store: Store<EntityStore?>,
        p3: CommandBuffer<EntityStore?>
    ) {
        val damageSource = deathComonent.deathInfo?.source

        if (damageSource is Damage.EntitySource) {
            SpawnManager.get().addKilledZombie()

            val playerRef = store.getComponent(damageSource.ref, PlayerRef.getComponentType())
            HytaleLogger.getLogger().atInfo().log("Zombie killed by ${playerRef?.username}")
            HytaleLogger.getLogger().atInfo().log("Zombies killed ${SpawnManager.get().zombiesKilled.get()}")


            if (SpawnManager.get().zombiesKilled.get() == SpawnManager.get().zombies) {
                EventTitleUtil.showEventTitleToPlayer(playerRef!!, Message.raw("Round 2"), Message.raw(""), true);
            }
        }
    }

    override fun getQuery(): Query<EntityStore?>? {
        return Query.and(NPCEntity.getComponentType(), UUIDComponent.getComponentType())
    }


}