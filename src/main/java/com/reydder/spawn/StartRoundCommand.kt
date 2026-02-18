package com.reydder.spawn

import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.concurrent.CompletableFuture


class StartRoundCommand: AbstractAsyncCommand("startround", "") {
    override fun executeAsync(context: CommandContext): CompletableFuture<Void?> {
        val ref = context.senderAsPlayerRef()
        val store = ref?.store
        val world = store?.getExternalData()?.world

        if (store != null && world != null) {
            return this.runAsync(context, {
                SpawnManager.get().startRound()
            }, world)
        }

        return CompletableFuture.completedFuture(null)
    }
}