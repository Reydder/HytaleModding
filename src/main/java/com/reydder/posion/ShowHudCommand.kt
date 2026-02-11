package com.reydder.posion

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.packets.interface_.HudComponent
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.commands.debug.HudManagerTestCommand
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.logging.Level

class ShowHudCommand(name: String, description: String): AbstractPlayerCommand(name, description) {


    override fun execute(
        commandContext: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val playerComponent = store.getComponent<Player?>(ref, Player.getComponentType())

        val hudManager = playerComponent?.getHudManager()
        HudComponent.entries.forEach {
            hudManager?.hideHudComponents(playerRef, it)
        }
        HytaleLogger.getLogger().at(Level.INFO)?.log("Show ammo hud")
    }
}