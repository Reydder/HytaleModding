package com.reydder.spawn.commands

import com.hypixel.hytale.component.RemoveReason
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.reydder.spawn.GameManager
import java.util.concurrent.CompletableFuture

class ResetRoundCommand: AbstractAsyncCommand("resetround", "") {
    override fun executeAsync(commandContext: CommandContext): CompletableFuture<Void?> {
        val ref = commandContext.senderAsPlayerRef()
        val store = ref?.store
        val world = store?.getExternalData()?.world

        if (store != null && world != null) {
            return this.runAsync(commandContext, {
                GameManager.get().reset(world.name, store)
            }, world)
        }

        return CompletableFuture.completedFuture(null)
    }
}