package com.reydder.poison

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class ProjectilePoisonSystem(val componentType: ComponentType<EntityStore?, PoisonComponent>): EntityEventSystem<EntityStore, Damage>(Damage::class.java) {

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

        val projectSource = event.source as? Damage.ProjectileSource
        projectSource?.let { projectileSource ->
            val poisonComponent = store.getComponent(entity, componentType)
            if (poisonComponent == null) {
                commandbuffer.addComponent(entity, componentType)
            }
        }
    }
}