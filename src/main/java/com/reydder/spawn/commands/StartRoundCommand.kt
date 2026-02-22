package com.reydder.spawn.commands

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.inventory.ItemStack
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
import com.reydder.spawn.GameManager
import java.util.concurrent.CompletableFuture

class StartRoundCommand: AbstractAsyncCommand("startround", "") {
    override fun executeAsync(context: CommandContext): CompletableFuture<Void?> {
        val ref = context.senderAsPlayerRef()
        val store = ref?.store
        val world = store?.getExternalData()?.world

        HytaleLogger.getLogger().atInfo().log("Round started at Thread: ${Thread.currentThread().name}")

        if (store != null && world != null) {
            return this.runAsync(context, {
                HytaleLogger.getLogger().atInfo().log("Round started at Thread: ${Thread.currentThread().name}")
                GameManager.get().startGame(world.name)
                store.forEachEntityParallel { index, archeTypeChunk, commandBuffer ->
                    world.execute {
                        val player = store.getComponent(ref, Player.getComponentType())
                        if (player == null) return@execute

                        val entityStatMap = store.getComponent(ref, EntityStatMap.getComponentType())
                        val pointsStatIndex = EntityStatType.getAssetMap()?.getIndex("Points")
                        val ammoStatIndex = EntityStatType.getAssetMap()?.getIndex("Rifle_Ammo")
                        pointsStatIndex?.let { pointsIndex ->
                            entityStatMap?.setStatValue(pointsIndex, 0F)
                        }

                        ammoStatIndex?.let { ammoIndex ->
                            entityStatMap?.setStatValue(ammoIndex, 30F)
                        }

                        player.inventory.combinedHotbarFirst.forEach {index, stack ->
                            player.inventory.combinedHotbarFirst.removeItemStackFromSlot(index)
                        }

                        val rifleItem = ItemStack("Weapon_Assault_Rifle")
                        val bullets = ItemStack("Weapon_Bullet", 100)

                        player.inventory.combinedHotbarFirst.addItemStack(rifleItem)
                        player.inventory.combinedHotbarFirst.addItemStack(bullets)
                    }
                }
            }, world)
        }

        return CompletableFuture.completedFuture(null)
    }
}