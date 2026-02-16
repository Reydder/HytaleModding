package com.reydder.shop.Instructions.Actions

import com.google.gson.JsonElement
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport
import com.hypixel.hytale.server.npc.asset.builder.InstructionType
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase
import com.hypixel.hytale.server.npc.instructions.Action
import java.util.*
import javax.annotation.Nonnull

class BuilderActionOpenWeaponShop: BuilderActionBase() {

    @Nonnull
    override fun getShortDescription(): String {
        return "Open the barter shop UI for the current player"
    }

    @Nonnull
    override fun getLongDescription(): String {
        return this.getShortDescription()
    }

    @Nonnull
    override fun build(@Nonnull builderSupport: BuilderSupport): Action {
        return ActionOpenWeaponShop(this)
    }

    @Nonnull
    override fun getBuilderDescriptorState(): BuilderDescriptorState {
        return BuilderDescriptorState.Stable
    }
}