package com.daderpduck.hallucinocraft;

import com.daderpduck.hallucinocraft.drugs.capabilities.DrugCapability;
import com.daderpduck.hallucinocraft.drugs.capabilities.DrugDefinition;
import com.daderpduck.hallucinocraft.drugs.capabilities.DrugHandler;
import com.daderpduck.hallucinocraft.network.PacketHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class ForgeEventHandler
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addGenericListener(ItemStack.class, ForgeEventHandler::attachItemCapabilities);
        bus.addListener(ForgeEventHandler::addReloadListeners);
        bus.addListener(ForgeEventHandler::onDataPackSync);
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();
        if (!stack.isEmpty())
        {
            DrugDefinition drug = DrugCapability.get(stack);
            if (drug != null)
            {
                event.addCapability(DrugCapability.KEY, new DrugHandler(drug.getData()));
            }
        }
    }

    public static void addReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(DrugCapability.MANAGER);
    }

    public static void onDataPackSync(OnDatapackSyncEvent event)
    {
        // Sync managers
        final ServerPlayer player = event.getPlayer();
        final PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);

        PacketHandler.send(target, DrugCapability.MANAGER.createSyncPacket());
    }

    // Todo: prevent sleeping when high on caffeine
    /*public static void onPlayerSleepInBed(PlayerSleepInBedEvent event)
    {
        event.setResult(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
        event.getPlayer().sendMessage(new TextComponent("You are too high on caffeine!"), Util.NIL_UUID);
        return;
    }*/
}
