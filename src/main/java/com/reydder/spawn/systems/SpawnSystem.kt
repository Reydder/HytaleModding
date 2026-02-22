package com.reydder.spawn.systems

import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.DelayedSystem
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.NPCPlugin
import com.reydder.spawn.GameManager
import com.reydder.spawn.data.GameConfig
import kotlin.random.Random

class SpawnSystem: DelayedSystem<EntityStore>(1.0F) {

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
                    if (gameManager.spawnTimeInterval <= 0F) {
                        val zombieToSpawn = Math.min(
                            gameManager.spawnPerTick,
                            gameManager.zombies - gameManager.spawnedZombies.get()
                        )

                        for (i in 0..<zombieToSpawn) {
                            val zombieType = gameConfig.zombieTypes.random()
                            val closestSpawnPoints = getPlayersClosestSpawnPoints(store, gameConfig.spawnPoints)

                            if (closestSpawnPoints.isEmpty()) break

                            val spawnPoint = closestSpawnPoints.random()

                            val position = randomizeSpawnPosition(
                                Vector3d(
                                    spawnPoint.x.toDouble(),
                                    spawnPoint.y.toDouble(),
                                    spawnPoint.z.toDouble()
                                )
                            )
                            val rotation = Vector3f()

                            val pair = NPCPlugin.get().spawnNPC(store, zombieType, null, position, rotation)
                            if (pair != null && pair.first() != null && pair.first().isValid) {
                                gameManager.zombieSpawned()
                                HytaleLogger.getLogger().atInfo().log("-------")
                                HytaleLogger.getLogger().atInfo()
                                    .log("Zombie spawned at thread: ${Thread.currentThread().name}")
                                HytaleLogger.getLogger().atInfo()
                                    .log("Zombie spawned at ${position} in ${store.externalData.world.name}")
                                HytaleLogger.getLogger().atInfo().log("-------")
                            }
                        }

                        gameManager.setNewReducedSpawnTimeInterval()
                    } else {
                        gameManager.decreaseSpawnTimeInterval(dt)
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

    private fun getPlayersClosestSpawnPoints(store: Store<EntityStore?>, spawnPoints: List<GameConfig.MapConfig.SpawnPoint>): Set<GameConfig.MapConfig.SpawnPoint> {
        val closestSpawnPoints: MutableSet<GameConfig.MapConfig.SpawnPoint> = mutableSetOf()

        store.forEachEntityParallel { index, archetypeChunk, commandBuffer ->
            val player = archetypeChunk.getComponent(index, Player.getComponentType()) ?: return@forEachEntityParallel
            val transformComponent = archetypeChunk.getComponent(index, TransformComponent.getComponentType()) ?: return@forEachEntityParallel
            for (position in spawnPoints) {
                val vector3d = Vector3d(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
                val distance = transformComponent.position.distanceTo(vector3d)

                if (distance <= 25.0) {
                    closestSpawnPoints.add(position)
                }
            }
        }

        HytaleLogger.getLogger().atInfo().log("Closest spawn points: ${closestSpawnPoints}")

        return closestSpawnPoints
    }
}