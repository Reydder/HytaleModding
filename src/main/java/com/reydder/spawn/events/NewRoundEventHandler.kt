package com.reydder.spawn.events

import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.universe.world.events.WorldEvent
import com.hypixel.hytale.server.core.util.EventTitleUtil
import com.reydder.spawn.GameManager

class NewRoundEventHandler private constructor() {

    companion object {
        @JvmStatic
        fun onNewRoundStarted(event: NewRoundEvent) {
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