package com.reydder;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketWatcher;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.projectile.component.Projectile;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.reydder.ammo.interactions.ReloadConditionInteraction;
import com.reydder.ammo.interactions.ReloadInteraction;
import com.reydder.ecsSystems.InputLogSystem;
import com.reydder.events.OnPlayerReadyEvent;
import com.reydder.ammo.commands.ShowGUICommand;
import com.reydder.ammo.systems.UpdateAmmoIndicatorSystem;
import com.reydder.ammo.commands.UpdateGUICommand;
import com.reydder.posion.PoisonCommand;
import com.reydder.posion.PoisonComponent;
import com.reydder.posion.PoisonSystem;
import com.reydder.posion.ShowHudCommand;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Arrays;
import java.util.logging.Level;

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

        this.getCommandRegistry().registerCommand(new PoisonCommand(""));
        this.getCommandRegistry().registerCommand(new MyCommand("test", ""));
        this.getCommandRegistry().registerCommand(new OtherCommand("hello", ""));
        componentType = this.getEntityStoreRegistry().registerComponent(PoisonComponent.class, PoisonComponent::new);
        this.getEntityStoreRegistry().registerSystem(new PoisonSystem(this.componentType));

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, OnPlayerReadyEvent::onPlayerReady);

        this.getCommandRegistry().registerCommand(new ShowHudCommand("showammo", ""));

        this.getEventRegistry().registerGlobal(LivingEntityInventoryChangeEvent.class, (e) -> {
            //HytaleLogger.getLogger().at(Level.INFO).log("Inventoru change");
        });

        this.getCommandRegistry().registerCommand(new ShowGUICommand("showgui"));
        this.getCommandRegistry().registerCommand(new UpdateGUICommand());

        this.getCodecRegistry(Interaction.CODEC).register("ReloadInteraction", ReloadInteraction.class, ReloadInteraction.getCODEC());
        this.getCodecRegistry(Interaction.CODEC).register("ReloadCondition", ReloadConditionInteraction.class, ReloadConditionInteraction.getCODEC());

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

    @Override
    protected void start() {
        this.getEntityStoreRegistry().registerSystem(new InputLogSystem(componentType));
        this.getEntityStoreRegistry().registerSystem(new UpdateAmmoIndicatorSystem());
    }
}


