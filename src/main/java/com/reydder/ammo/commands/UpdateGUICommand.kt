package com.reydder.ammo.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.reydder.ammo.gui.AmmoIndicator
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

class UpdateGUICommand: AbstractPlayerCommand("updategui", "") {

    private var newText: RequiredArg<String> = this.withRequiredArg<String>("text", "", ArgTypes.STRING)

    override fun execute(
        context: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val text = newText.get(context)

        val player = store.getComponent(ref, Player.getComponentType())
        val hudManager = player?.hudManager ?: return

        CompletableFuture.runAsync({
            if (hudManager.customHud != null) {
                //hudManager.resetHud(playerRef)
                hudManager.setCustomHud(playerRef, AmmoIndicator(playerRef, text))
                playerRef.sendMessage(Message.raw("Ammo indicator shown"))
            } else {
                HytaleLogger.getLogger().at(Level.INFO).log("Can not update UI")
            }
        }, world)
    }
}