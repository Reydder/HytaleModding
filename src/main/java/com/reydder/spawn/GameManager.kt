package com.reydder.spawn

import com.google.gson.Gson
import com.hypixel.hytale.server.core.HytaleServer
import com.hypixel.hytale.server.core.universe.Universe
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
    }

    var activeGames: ConcurrentHashMap<String, GameConfig.MapConfig> = ConcurrentHashMap()
        private set

    var zombiesKilled: AtomicInteger = AtomicInteger(0)
        private set
    var spawnedZombies: AtomicInteger = AtomicInteger(0)
        private set
    var activeZombies: AtomicInteger = AtomicInteger(0)

    var round: Int = 0
        private set
    var zombies: Int = 0
        private set
    var maxActiveZombies = 0
        private set
    var maxSpawnPerTick = 0
        private set
    var timeToStartSpawning = 0.0F
        private set

    fun startGame(worldName: String) {
        // Read GameConfig json file
        // TODO Configure path instead of hardcoding it
        val path = Paths.get("").resolve("Rounds").resolve("GameConfig.json")
        val json = Files.readString(path)

        val gameConfig = Gson().fromJson(json, GameConfig::class.java)
        val mapConfig = gameConfig.maps.first { it.worldName == worldName }

        round = 1
        zombies = 1
        maxActiveZombies = 2
        maxSpawnPerTick = 1
        timeToStartSpawning = 5.0F
        activeGames[worldName] = mapConfig

        val world = Universe.get().worlds.get(worldName.lowercase()) ?: return
        val eventDispatcher = HytaleServer.get().eventBus.dispatchFor(NewRoundEvent::class.java)
        eventDispatcher?.dispatch(NewRoundEvent(world))
    }

    fun canSpawnZombie(): Boolean {
        return spawnedZombies.get() < zombies
    }

    fun zombieKilled() {
        zombiesKilled.incrementAndGet()
        activeZombies.decrementAndGet()
    }

    fun zombieSpawned() {
        spawnedZombies.incrementAndGet()
        activeZombies.incrementAndGet()
    }

    fun nextRound(game: String) {
        round++
        zombies++
        maxActiveZombies++
        maxSpawnPerTick++
        zombiesKilled.set(0)
        activeZombies.set(0)
        spawnedZombies.set(0)
        timeToStartSpawning = 5.0F

        val world = Universe.get().worlds.get(game.lowercase()) ?: return
        val eventDispatcher = HytaleServer.get().eventBus.dispatchFor(NewRoundEvent::class.java)
        eventDispatcher?.dispatch(NewRoundEvent(world))
    }

    fun decreaseTimeToStartSpawning(dt: Float) {
        timeToStartSpawning -= dt
    }

    fun reset(game: String) {
        round = 0
        zombies = 0
        maxActiveZombies = 0
        maxSpawnPerTick = 0
        zombiesKilled.set(0)
        activeZombies.set(0)
        spawnedZombies.set(0)
        timeToStartSpawning = 0.0F
        activeGames.remove(game)
    }

}