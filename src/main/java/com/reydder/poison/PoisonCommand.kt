package com.reydder.poison

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.reydder.MyPlugin

class PoisonCommand(): AbstractPlayerCommand("poison",  "") {

    override fun execute(
        ctx: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val player: Player? = store.getComponent(ref, Player.getComponentType())

        val poisonComponent = PoisonComponent(3F, 0.5F, 8)
        world.execute {
            store.addComponent(ref, MyPlugin.instance.componentType, poisonComponent)
        }
        player?.sendMessage(Message.raw("You have been poisoned"))
    }
}