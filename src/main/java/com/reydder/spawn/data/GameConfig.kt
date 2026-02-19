package com.reydder.spawn.data

data class GameConfig(val maps: List<MapConfig>) {
    data class MapConfig(
        val worldName: String,
        val spawnPoints: List<SpawnPoint>,
        val zombieTypes: List<String>,
        val zombiesStart: Int = 15,
        val maxZombiesPerRound: Int = 45,
        val roundsToIncreaseMaxZombies: Int = 5,
        val zombiesToIncrease: Int = 5,
        val spawnPerTickStart: Int = 3,
        val maxSpawnPerTick: Int = 10,
        val roundsToIncreaseSPT: Int = 3,
        val spawnPerTickToIncrease: Int = 1,
    ) {
        data class SpawnPoint(val x: Int, val y: Int, val z: Int)
    }
}
