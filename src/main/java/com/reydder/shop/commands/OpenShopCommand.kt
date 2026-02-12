package com.reydder.shop.commands

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.reydder.shop.gui.WeaponsShopPage
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier


class OpenShopCommand: AbstractPlayerCommand("openshop", "") {
    override fun execute(
        commandContext: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val player = store.getComponent(ref, Player.getComponentType())

        player?.pageManager?.openCustomPage(ref, store, WeaponsShopPage(playerRef))
    }
}