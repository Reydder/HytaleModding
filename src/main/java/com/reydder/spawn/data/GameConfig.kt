package com.reydder.spawn.data

data class GameConfig(val maps: List<MapConfig>) {
    data class MapConfig(
        val worldName: String,
        val spawnPoints: List<SpawnPoint>,
        val zombieTypes: List<String>
    ) {
        data class SpawnPoint(val x: Int, val y: Int, val z: Int)
    }
}
