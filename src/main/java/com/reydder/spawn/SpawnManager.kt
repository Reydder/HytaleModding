package com.reydder.spawn

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.atomics.AtomicInt

class SpawnManager private constructor() {
    companion object {
        private lateinit var instance: SpawnManager

        @JvmStatic
        fun get(): SpawnManager {
            if (!::instance.isInitialized) {
                instance = SpawnManager()
            }

            return instance
        }
    }

    var zombiesKilled: AtomicInteger = AtomicInteger(0)
    var spawnedZombies: AtomicInteger = AtomicInteger(0)

    var round: Int = 1
    var zombies: Int = 15
    var maxActiveZombies = 5

    var started = AtomicBoolean(false)

    fun addKilledZombie() {
        zombiesKilled.incrementAndGet()
    }

    fun zombieSpawned() {
        spawnedZombies.incrementAndGet()
    }

    fun startRound() {
        started.set(true)
    }

    fun finishRound() {
        started.set(false)
    }

    fun reset() {
        round = 1
        started.set(false)
        zombiesKilled.set(0)
        spawnedZombies.set(0)

    }

}