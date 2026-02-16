package com.reydder.shop.Instructions.Actions

import com.hypixel.hytale.builtin.adventure.shop.barter.BarterPage
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.corecomponents.ActionBase
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase
import com.hypixel.hytale.server.npc.role.Role
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider
import com.reydder.shop.gui.WeaponsShopPage
import javax.annotation.Nonnull

class ActionOpenWeaponShop(builderActionBase: BuilderActionBase): ActionBase(builderActionBase) {

    override fun canExecute(
        @Nonnull ref: Ref<EntityStore?>,
        @Nonnull role: Role,
        sensorInfo: InfoProvider?,
        dt: Double,
        @Nonnull store: Store<EntityStore?>
    ): Boolean {
        return super.canExecute(ref, role, sensorInfo, dt, store) && role.getStateSupport()
            .getInteractionIterationTarget() != null
    }

    override fun execute(
        @Nonnull ref: Ref<EntityStore?>,
        @Nonnull role: Role,
        sensorInfo: InfoProvider?,
        dt: Double,
        @Nonnull store: Store<EntityStore?>
    ): Boolean {
        super.execute(ref, role, sensorInfo, dt, store)
        val playerReference = role.getStateSupport().getInteractionIterationTarget()

        if (playerReference == null) {
            return false
        } else {
            val playerRefComponent = store.getComponent<PlayerRef?>(playerReference, PlayerRef.getComponentType())
            if (playerRefComponent == null) {
                return false
            } else {
                val playerComponent = store.getComponent<Player?>(playerReference, Player.getComponentType())
                if (playerComponent == null) {
                    return false
                } else {
                    val refIsPlayer = store.getComponent(ref, Player.getComponentType()) != null
                    val playerRefIsPlayer = store.getComponent(playerReference, Player.getComponentType()) != null


                    playerComponent.getPageManager() .openCustomPage(playerReference, store, WeaponsShopPage(playerRefComponent))
                    //playerComponent.getPageManager() .openCustomPage(ref, store, BarterPage(playerRefComponent, "Klops_Merchant"))
                    return true
                }
            }
        }
    }
}