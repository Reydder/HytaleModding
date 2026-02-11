package com.reydder.ecsSystems

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.component.system.EventSystem
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem
import com.hypixel.hytale.server.core.modules.entity.system.EntitySystems
import com.hypixel.hytale.server.core.modules.projectile.ProjectileModule
import com.hypixel.hytale.server.core.modules.projectile.component.Projectile
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.logging.Level

class DamageBlockEventSystem: EventSystem<DamageBlockEvent>(DamageBlockEvent::class.java) {


    override fun shouldProcessEvent(event: DamageBlockEvent): Boolean {



        return true
    }


}

class ProjectLaunchSystem: EntityTickingSystem<EntityStore>() {

    override fun tick(
        p0: Float,
        p1: Int,
        p2: ArchetypeChunk<EntityStore?>,
        p3: Store<EntityStore?>,
        p4: CommandBuffer<EntityStore?>
    ) {
        HytaleLogger.getLogger().at(Level.INFO).log("Projectile")
    }



    override fun getQuery(): Query<EntityStore?>? {
        return Projectile.getComponentType()
    }


}