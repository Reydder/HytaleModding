package com.reydder;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.IBaseEvent;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public class ExampleEvent {


    public static void onPlayerReady(PlayerMouseButtonEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Message.raw(player.getDisplayName() + " clicked"));
    }
}
