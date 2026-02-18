package com.reydder.poison

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class PoisonSystem(
    private val poisonComponentType: ComponentType<EntityStore?, PoisonComponent>
): EntityTickingSystem<EntityStore>() {



    override fun tick(
        dt: Float,
        index: Int,
        archetypeChunk: ArchetypeChunk<EntityStore?>,
        store: Store<EntityStore?>,
        commandBuffer: CommandBuffer<EntityStore?>
    ) {
        val poison: PoisonComponent? = archetypeChunk.getComponent(index, poisonComponentType)
        val ref: Ref<EntityStore?> = archetypeChunk.getReferenceTo(index)

        poison?.addElapsedTime(dt)

        if (poison != null && poison.getElapsedTime() >= poison.getTickInterval()) {

            poison.resetElapsedTime()

            val damage = Damage(Damage.NULL_SOURCE, DamageCause.OUT_OF_WORLD!!, poison.getDamagePerTick())


            DamageSystems.executeDamage(ref, commandBuffer, damage)

            poison.decrementRemainingTicks()
        }

        if (poison?.isExpired() == true) {
            commandBuffer.removeComponent(ref, poisonComponentType)
        }
    }

    override fun getQuery(): Query<EntityStore?> {
        return poisonComponentType
    }
}