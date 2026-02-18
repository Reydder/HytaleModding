package com.reydder.spawn

import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand
import java.util.concurrent.CompletableFuture

class ResetRoundCommand: AbstractAsyncCommand("resetround", "") {
    override fun executeAsync(commandContext: CommandContext): CompletableFuture<Void?> {
        val ref = commandContext.senderAsPlayerRef()
        val store = ref?.store
        val world = store?.getExternalData()?.world

        if (store != null && world != null) {
            return this.runAsync(commandContext, {
                SpawnManager.get().reset()
            }, world)
        }

        return CompletableFuture.completedFuture(null)
    }
}