package com.reydder.spawn

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.DelayedSystem
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.component.system.tick.TickingSystem
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.NPCPlugin
import com.hypixel.hytale.server.npc.systems.BlackboardSystems
import sun.awt.Mutex
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SpawnSystem: DelayedSystem<EntityStore>(3.0F) {
    companion object {
        val WORLD = "instance-NPC_Gym-6c3376de-34e8-48dc-b063-ee65bb9c6e22"
    }

    override fun delayedTick(
        dt: Float,
        index: Int,
        store: Store<EntityStore?>
    ) {
        if (store.externalData.world.name != WORLD) return

        val spawnManager = SpawnManager.get()

        if (spawnManager.started.get() == false) return

        if (spawnManager.spawnedZombies.get() < spawnManager.zombies) {
            val position = Vector3d(1.0, 103.0, 3.0)
            val rotation = Vector3f()

            val pair = NPCPlugin.get().spawnNPC(store, "Zombie", null, position, rotation)
            HytaleLogger.getLogger().atInfo().log("${pair?.first() == null}")
            if (pair != null && pair.first() != null && pair.first().isValid) {
                spawnManager.zombieSpawned()
            }
        }
    }

}