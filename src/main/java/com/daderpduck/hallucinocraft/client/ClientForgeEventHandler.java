package com.daderpduck.hallucinocraft.client;

import java.util.List;

import com.daderpduck.hallucinocraft.drugs.capabilities.DrugCapability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientForgeEventHandler
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientForgeEventHandler::onItemTooltip);
    }

    @SuppressWarnings("ConstantConditions")
    public static void onItemTooltip(ItemTooltipEvent event)
    {
        final ItemStack stack = event.getItemStack();
        final List<Component> text = event.getToolTip();
        if (!stack.isEmpty())
        {
            stack.getCapability(DrugCapability.CAPABILITY).ifPresent(cap -> cap.addTooltipInfo(stack, text));
        }
    }
}
