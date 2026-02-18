package com.reydder.spawn.commands

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand
import com.reydder.spawn.GameManager
import java.util.concurrent.CompletableFuture

class StartRoundCommand: AbstractAsyncCommand("startround", "") {
    override fun executeAsync(context: CommandContext): CompletableFuture<Void?> {
        val ref = context.senderAsPlayerRef()
        val store = ref?.store
        val world = store?.getExternalData()?.world

        if (store != null && world != null) {
            return this.runAsync(context, {
                HytaleLogger.getLogger().atInfo().log("Started ${world.name}")
                GameManager.get().startGame(world.name)
            }, world)
        }

        return CompletableFuture.completedFuture(null)
    }
}