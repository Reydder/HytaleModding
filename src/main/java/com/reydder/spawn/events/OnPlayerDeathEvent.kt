package com.reydder.spawn.events

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.event.events.player.PlayerEvent
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class OnPlayerDeathEvent(ref: Ref<EntityStore>, player: Player): PlayerEvent<EntityStore>(ref, player) {


    companion object {
        @JvmStatic
        fun onPlayerDeath(event: OnPlayerDeathEvent) {
            HytaleLogger.getLogger().atInfo().log("Player dead at Thread: ${Thread.currentThread().name}")

            val store = event.playerRef.store
            val world = store.externalData.world
        }
    }

}