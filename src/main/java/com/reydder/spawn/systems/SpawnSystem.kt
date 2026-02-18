package com.reydder.spawn.systems

import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.DelayedSystem
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.NPCPlugin
import com.reydder.spawn.GameManager
import kotlin.random.Random

class SpawnSystem: DelayedSystem<EntityStore>(3.0F) {

    override fun delayedTick(
        dt: Float,
        index: Int,
        store: Store<EntityStore?>
    ) {
        val gameManager = GameManager.get()
        for ((worldName, gameConfig) in GameManager.get().activeGames) {
            if (worldName != store.externalData.world.name) break

            if (gameManager.canSpawnZombie()) {
                if (gameManager.timeToStartSpawning > 0.0F) {
                    gameManager.decreaseTimeToStartSpawning(dt)
                    HytaleLogger.getLogger().atInfo().log("---------------")
                    HytaleLogger.getLogger().atInfo().log("${gameManager.timeToStartSpawning}")
                    HytaleLogger.getLogger().atInfo().log("---------------")
                }

                if (gameManager.timeToStartSpawning <= 0.0F) {
                    val zombieType = gameConfig.zombieTypes.random()

                    val zombieToSpawn = Math.min(
                        gameManager.maxSpawnPerTick,
                        gameManager.zombies - gameManager.spawnedZombies.get()
                    )

                    for (i in 0..<zombieToSpawn) {
                        // TODO Get closest position to player
                        val spawnPoint = gameConfig.spawnPoints.random()
                        // TODO Randomize position
                        val position = randomizeSpawnPosition(Vector3d(spawnPoint.x.toDouble(), spawnPoint.y.toDouble(), spawnPoint.z.toDouble()))
                        val rotation = Vector3f()

                        val pair = NPCPlugin.get().spawnNPC(store, zombieType, null, position, rotation)
                        if (pair != null && pair.first() != null && pair.first().isValid) {
                            gameManager.zombieSpawned()
                            HytaleLogger.getLogger().atInfo().log("Zombie spawned at ${position} in ${store.externalData.world.name}")
                        }
                    }
                }
            }
        }
    }

    private fun randomizeSpawnPosition(position: Vector3d): Vector3d {
        val x = Random.nextDouble(-3.0, 3.0) + position.x
        val z = Random.nextDouble(-3.0, 3.0) + position.z

        return Vector3d(x, position.y, z)
    }
}