package com.reydder.spawn.events

import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.events.WorldEvent
import com.hypixel.hytale.server.core.util.EventTitleUtil
import com.reydder.spawn.GameManager

class NewRoundEvent(world: World): WorldEvent(world) {

    companion object {
        @JvmStatic
        fun onNewRoundStarted(event: NewRoundEvent) {
            HytaleLogger.getLogger().atInfo().log("New round started at Thread: ${Thread.currentThread().name}")
            HytaleLogger.getLogger().atInfo().log("Round ${GameManager.get().round} started ")

            val world = event.world
            EventTitleUtil.showEventTitleToWorld(
                Message.raw("${GameManager.get().round}"),
                Message.raw("Round"),
                true,
                null,
                3.0F,
                1.0F,
                1.0F,
                world.entityStore.store
            )
        }
    }
}