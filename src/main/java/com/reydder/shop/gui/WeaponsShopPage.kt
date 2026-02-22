package com.reydder.shop.gui

import com.hypixel.hytale.assetstore.AssetRegistry
import com.hypixel.hytale.assetstore.AssetStore
import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.asset.type.item.config.Item
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.inventory.ItemStack
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackTransaction
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
import com.hypixel.hytale.server.core.ui.builder.EventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.function.Supplier

class WeaponsShopPage(playerRef: PlayerRef) : InteractiveCustomUIPage<ShopData>(playerRef, CustomPageLifetime.CanDismiss, ShopData.CODEC) {

    companion object {
        private const val BULLET_PRICE = 100F
        private const val MEDKIT_PRICE = 2000F
        private const val RIFLE_PRICE = 1000F
    }

    override fun build(
        ref: Ref<EntityStore?>,
        uiCommandBuilder: UICommandBuilder,
        uiEventBuilder: UIEventBuilder,
        store: Store<EntityStore?>
    ) {
        val player = store.getComponent(ref, Player.getComponentType()) ?: return
        val entityStatMap = store.getComponent(ref, EntityStatMap.getComponentType()) ?: return

        val pointsIndex = EntityStatType.getAssetMap()?.getIndex("Points") ?: return
        val points = entityStatMap.get(pointsIndex)?.get()

        val noPointsForBullets = points != null && points < BULLET_PRICE
        val noPointsForMedKit = points != null && points < MEDKIT_PRICE

        val inventory = player.inventory.combinedHotbarFirst

        var hasRifle = false
        for (i in 0..< inventory.capacity) {
            val itemStack = inventory.getItemStack(i.toShort())
            if (itemStack?.itemId == "Weapon_Assault_Rifle") {
                hasRifle = true
                break
            }
        }

        uiCommandBuilder.append("Pages/WeaponShop.ui")
        uiCommandBuilder.set("#BuyRifle.Disabled", hasRifle)
        uiCommandBuilder.set("#BuyBullet.Disabled", noPointsForBullets)
        uiCommandBuilder.set("#BuyMedkit.Disabled", noPointsForMedKit)

        // TODO: Remove when this when alm weapons implemented
        uiCommandBuilder.set("#RifleItem.Visible", false)

        // Events
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyBullet", EventData().append("ItemId", "Weapon_Bullet"))
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyRifle", EventData().append("ItemId", "Weapon_Assault_rifle"))
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyMedkit", EventData().append("ItemId", "Full_Heal_Potion"))
    }

    override fun handleDataEvent(ref: Ref<EntityStore>, store: Store<EntityStore>, data: ShopData) {
        val item = AssetRegistry.getAssetStore(Item::class.java)?.assetMap?.getAsset(data.itemId) ?: return
        val player = store.getComponent(ref, Player.getComponentType()) ?: return

        val entityStatMap = store.getComponent(ref, EntityStatMap.getComponentType()) ?: return
        val pointsIndex = EntityStatType.getAssetMap()?.getIndex("Points") ?: return

        val uiCommandBuilder = UICommandBuilder()

        var itemTransaction: ItemStackTransaction

        when(item.id)  {
           "Weapon_Bullet" -> {
               itemTransaction = player.inventory.combinedStorageFirst.addItemStack(ItemStack(item.id, 50))
               entityStatMap.addStatValue(pointsIndex, -BULLET_PRICE)

               val points = entityStatMap.get(pointsIndex)?.get()
               val noPointsForBullets = points != null && points < BULLET_PRICE
               uiCommandBuilder.set("#BuyBullet.Disabled", noPointsForBullets) }
            "Weapon_Assault_Rifle" -> {
                itemTransaction = player.inventory.combinedHotbarFirst.addItemStack(ItemStack(item.id))
                entityStatMap.addStatValue(pointsIndex, -RIFLE_PRICE)
                uiCommandBuilder.set("#BuyRifle.Disabled", true)
            }
            else -> {
                itemTransaction = player.inventory.combinedHotbarFirst.addItemStack(ItemStack(item.id))
                entityStatMap.addStatValue(pointsIndex, -MEDKIT_PRICE)

                val points = entityStatMap.get(pointsIndex)?.get()
                val noPointsForMedKit = points != null && points < MEDKIT_PRICE
                uiCommandBuilder.set("#BuyMedkit.Disabled", noPointsForMedKit)
            }
        }

        if (itemTransaction.succeeded()) {
            HytaleLogger.getLogger().atInfo().log("Item purchased: ${item.id}")
            sendUpdate(uiCommandBuilder)
        }
    }
}

class ShopData {
    var itemId: String? = null

    companion object {
        val CODEC: BuilderCodec<ShopData?> = BuilderCodec.builder<ShopData?>(ShopData::class.java, Supplier { ShopData() })
            .append<String?>(
                KeyedCodec<String?>("ItemId", Codec.STRING),
                { data: ShopData?, value: String? -> data?.itemId = value },
                { data: ShopData? -> data?.itemId })
            .add()
            .build()
    }
}