package com.reydder.events

import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.event.events.player.PlayerEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent

class OnPlayerReadyEvent: IBaseEvent<PlayerReadyEvent> {

    companion object {
        @JvmStatic
        public fun onPlayerReady(event: PlayerReadyEvent) {
            val player = event.player
            val ref = event.playerRef
            val store = ref.store
            val world = player.world

            player.sendMessage(Message.raw("Hello ${ref}"))

            world?.execute {

            }
        }
    }
}