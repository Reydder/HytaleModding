package com.reydder.guns.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.reydder.guns.gui.AmmoIndicator
import java.util.concurrent.CompletableFuture

class ShowGUICommand(name: String): AbstractPlayerCommand(name, "") {
    override fun execute(
        context: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val player = store.getComponent(ref, Player.getComponentType())
        val hudManager = player?.hudManager ?: return

        CompletableFuture.runAsync( {
            if (hudManager.customHud == null) {
                hudManager.setCustomHud(playerRef, AmmoIndicator(playerRef))
                playerRef.sendMessage(Message.raw("Ammo indicator shown"))
            } else {

            }
        }, world)
    }
}