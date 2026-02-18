package com.reydder;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketWatcher;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.projectile.component.Projectile;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.reydder.guns.interactions.ReloadConditionInteraction;
import com.reydder.guns.interactions.ReloadInteraction;
import com.reydder.poison.ProjectilePoisonSystem;
import com.reydder.guns.commands.ShowGUICommand;
import com.reydder.guns.systems.UpdateAmmoIndicatorSystem;
import com.reydder.guns.commands.UpdateGUICommand;
import com.reydder.poison.PoisonCommand;
import com.reydder.poison.PoisonComponent;
import com.reydder.shop.Instructions.Actions.BuilderActionOpenWeaponShop;
import com.reydder.shop.commands.OpenShopCommand;
import com.reydder.spawn.*;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Arrays;

public class MyPlugin extends JavaPlugin {

    public static MyPlugin instance;

    private ComponentType<EntityStore, PoisonComponent> componentType;
    private ComponentType<EntityStore, Projectile> projectileComponent;

    public MyPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);

        instance = this;
    }

    @Override
    protected void setup() {
        super.setup();

        // Rounds
        this.getEntityStoreRegistry().registerSystem(new ZombieDeadSystem());
        this.getEntityStoreRegistry().registerSystem(new SpawnSystem());

        this.getCommandRegistry().registerCommand(new StartRoundCommand());
        this.getCommandRegistry().registerCommand(new ResetRoundCommand());

        // Guns
        this.getCommandRegistry().registerCommand(new ShowGUICommand("showgui"));
        this.getCommandRegistry().registerCommand(new UpdateGUICommand());

        this.getEntityStoreRegistry().registerSystem(new UpdateAmmoIndicatorSystem());

        this.getCodecRegistry(Interaction.CODEC).register("ReloadInteraction", ReloadInteraction.class, ReloadInteraction.getCODEC());
        this.getCodecRegistry(Interaction.CODEC).register("ReloadCondition", ReloadConditionInteraction.class, ReloadConditionInteraction.getCODEC());

        // Shop
        this.getCommandRegistry().registerCommand(new OpenShopCommand());
        NPCPlugin.get().registerCoreComponentType("OpenWeaponShop", BuilderActionOpenWeaponShop::new);

        // Poison
        this.getCommandRegistry().registerCommand(new PoisonCommand(""));
        componentType = this.getEntityStoreRegistry().registerComponent(PoisonComponent.class, PoisonComponent::new);
        this.getEntityStoreRegistry().registerSystem(new ProjectilePoisonSystem(componentType));

        // Other
        registerPacketWatcher();
    }

    private void registerPacketWatcher() {
        int[] omittedIds = {3, 108};

        PacketAdapters.registerInbound((PacketWatcher) (packetHandler, packet) -> {
            if (Arrays.stream(omittedIds).noneMatch(n -> n == packet.getId())) {
                //HytaleLogger.getLogger().at(Level.INFO).log("Packet hadnled" + packet.getId() + " " + packet.getClass().getName());

                //HytaleLogger.getLogger().at(Level.INFO).log("Packet hadnled" + packet.getClass().getName());

                if (!(packet instanceof SyncInteractionChains)) {
                    return;
                }

                SyncInteractionChains syncInteractionChains = (SyncInteractionChains) packet;
                for (SyncInteractionChain item : syncInteractionChains.updates) {
                    //HytaleLogger.getLogger().at(Level.INFO).log("Packet hadnled" + item.interactionType);
                    String itemId = item.itemInHandId;
                    /*if (Objects.equals(itemId, "Weapon_Assault_Rifle") && item.interactionType == InteractionType.Primary) {

                    }*/
                }
            }
        });
    }

    public ComponentType<EntityStore, PoisonComponent> getComponentType() {
        return this.componentType;
    }
}


