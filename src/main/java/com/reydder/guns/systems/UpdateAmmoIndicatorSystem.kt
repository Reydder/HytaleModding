package com.reydder.guns.systems

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.inventory.ItemStack
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.reydder.guns.gui.AmmoIndicator

class UpdateAmmoIndicatorSystem: EntityTickingSystem<EntityStore>() {

    override fun tick(
        delta: Float,
        index: Int,
        archetyeChunk: ArchetypeChunk<EntityStore?>,
        store: Store<EntityStore?>,
        commandBuffer: CommandBuffer<EntityStore?>
    ) {
        val player = archetyeChunk.getComponent(index, Player.getComponentType())
        val inventory = player?.inventory ?: return
        val playerRef = archetyeChunk.getComponent(index, PlayerRef.getComponentType()) ?: return

        val entityStatMap = archetyeChunk.getComponent(index, EntityStatMap.getComponentType())

        val rifleAmmoStatIndex = EntityStatType.getAssetMap().getIndex("Rifle_Ammo")
        val handgunAmmoStatIndex = EntityStatType.getAssetMap().getIndex("Handgun_Ammo")
        val pointsStatIndex = EntityStatType.getAssetMap().getIndex("Points")

        var bulletsAccount = 0

        for (i in 0..<inventory.combinedHotbarFirst.capacity) {
            val itemStack: ItemStack? = inventory.combinedHotbarFirst.getItemStack(i.toShort())
            if (itemStack?.itemId == "Weapon_Bullet") {
                bulletsAccount += itemStack.quantity
            }
        }

        if (inventory.activeHotbarItem?.itemId == "Weapon_Assault_Rifle") {
            player.hudManager.setCustomHud(
                playerRef,
                AmmoIndicator(
                    playerRef = playerRef,
                    text = "${entityStatMap?.get(rifleAmmoStatIndex)?.get()?.toInt()?.toString()}/$bulletsAccount",
                    points = entityStatMap?.get(pointsStatIndex)?.get() ?: 0F
                )
            )
        } else if (inventory.activeHotbarItem?.itemId == "Weapon_Handgun") {
            player.hudManager.setCustomHud(
                playerRef,
                AmmoIndicator(
                    playerRef = playerRef,
                    text = "${entityStatMap?.get(handgunAmmoStatIndex)?.get()?.toInt()?.toString()}/$bulletsAccount",
                    points = entityStatMap?.get(pointsStatIndex)?.get() ?: 0F
                )
            )
        } else {
            player.hudManager.setCustomHud(
                playerRef, AmmoIndicator(
                    playerRef = playerRef,
                    points = entityStatMap?.get(pointsStatIndex)?.get() ?: 0F)
            )
        }
    }

    override fun getQuery(): Query<EntityStore?>? {
        return Query.and(Player.getComponentType(), EntityStatMap.getComponentType())
    }
}