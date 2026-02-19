package com.reydder.spawn

import com.google.gson.Gson
import com.hypixel.hytale.component.RemoveReason
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.HytaleServer
import com.hypixel.hytale.server.core.universe.Universe
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.reydder.spawn.data.GameConfig
import com.reydder.spawn.events.NewRoundEvent
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class GameManager private constructor() {
    companion object {
        private lateinit var instance: GameManager

        @JvmStatic
        fun get(): GameManager {
            if (!::instance.isInitialized) {
                instance = GameManager()
            }

            return instance
        }

        private const val TIME_TO_START_SPAWN = 15.0F
        private const val SPAWN_TIME_INTERVAL = 10.0F
    }

    var activeGames: ConcurrentHashMap<String, GameConfig.MapConfig> = ConcurrentHashMap()
        private set

    var zombiesKilled: AtomicInteger = AtomicInteger(0)
        private set
    var spawnedZombies: AtomicInteger = AtomicInteger(0)
        private set

    var round: Int = 0
        private set
    var zombies: Int = 0
        private set
    var spawnPerTick: Int = 0
        private set
    var timeToStartSpawning = 0.0F
        private set
    var spawnTimeInterval = 0F
        private set
    var resetSpawnTimeInterval = 0F
        private set

    fun startGame(worldName: String) {
        // Read GameConfig json file
        // TODO Configure path instead of hardcoding it
        val path = Paths.get("").resolve("ZombiesGameConfig").resolve("GameConfig.json")
        val json = Files.readString(path)

        val gameConfig = Gson().fromJson(json, GameConfig::class.java)
        val mapConfig = gameConfig.maps.first { it.worldName == worldName }

        activeGames[worldName] = mapConfig

        round = 1

        zombies = mapConfig.zombiesStart
        spawnPerTick = mapConfig.spawnPerTickStart
        timeToStartSpawning = TIME_TO_START_SPAWN
        spawnTimeInterval = 0F
        resetSpawnTimeInterval = SPAWN_TIME_INTERVAL

        val world = Universe.get().worlds.get(worldName.lowercase()) ?: return
        val eventDispatcher = HytaleServer.get().eventBus.dispatchFor(NewRoundEvent::class.java)
        eventDispatcher?.dispatch(NewRoundEvent(world))
    }

    fun canSpawnZombie(): Boolean {
        return spawnedZombies.get() < zombies
    }

    @Synchronized
    fun zombieKilled() {
        zombiesKilled.incrementAndGet()
    }

    @Synchronized
    fun zombieSpawned() {
        spawnedZombies.incrementAndGet()
    }

    fun nextRound(game: String, store: Store<EntityStore?>) {
        val gameConfig = activeGames[game]

        if (gameConfig == null) {
            HytaleLogger.getLogger().atInfo().log("No game config found to begin next round for game: $game")
            reset(game, store)
            return
        }

        round++

        HytaleLogger.getLogger().atInfo().log("Round ${round}")

        zombies = (gameConfig.zombiesStart + ((round / gameConfig.roundsToIncreaseMaxZombies) * gameConfig.zombiesToIncrease))
            .coerceAtMost(gameConfig.maxZombiesPerRound)

        HytaleLogger.getLogger().atInfo().log("Zombies in round ${zombies}")

        spawnPerTick = (gameConfig.spawnPerTickStart + ((round / gameConfig.roundsToIncreaseSPT) * gameConfig.spawnPerTickToIncrease))
            .coerceAtMost(gameConfig.maxSpawnPerTick)

        HytaleLogger.getLogger().atInfo().log("Spawn Per tick reset to ${spawnPerTick}")

        zombiesKilled.set(0)
        spawnedZombies.set(0)
        timeToStartSpawning = TIME_TO_START_SPAWN
        spawnTimeInterval = 0F
        resetSpawnTimeInterval = SPAWN_TIME_INTERVAL

        val world = Universe.get().worlds.get(game.lowercase()) ?: return
        val eventDispatcher = HytaleServer.get().eventBus.dispatchFor(NewRoundEvent::class.java)
        eventDispatcher?.dispatch(NewRoundEvent(world))
    }

    @Synchronized
    fun decreaseTimeToStartSpawning(dt: Float) {
        timeToStartSpawning -= dt
    }

    @Synchronized
    fun decreaseSpawnTimeInterval(dt: Float) {
        spawnTimeInterval -= dt
        //HytaleLogger.getLogger().atInfo().log("Spawn Time Interval reduced to ${spawnTimeInterval}")
    }

    @Synchronized
    fun setNewReducedSpawnTimeInterval() {
        resetSpawnTimeInterval -= 1F
        spawnTimeInterval = resetSpawnTimeInterval.coerceAtLeast(3.0F)
        //HytaleLogger.getLogger().atInfo().log("Spawn Time Interval reset to ${spawnTimeInterval}")
    }

    fun reset(game: String, store: Store<EntityStore?>) {
        round = 0

        zombies = 0
        spawnPerTick = 0

        zombiesKilled.set(0)
        spawnedZombies.set(0)
        timeToStartSpawning = 0.0F
        spawnTimeInterval = 0F
        resetSpawnTimeInterval = 0.0F

        activeGames.remove(game)

        removeAllRemainingEnemies(store)
    }

    fun removeAllRemainingEnemies(store: Store<EntityStore?>) {
        val world = store.externalData.world

        // Remove all current zombies
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
    }

}