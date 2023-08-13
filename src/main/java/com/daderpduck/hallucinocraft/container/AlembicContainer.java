package com.daderpduck.hallucinocraft.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.ButtonHandlerContainer;
import net.dries007.tfc.common.container.CallbackSlot;

import org.jetbrains.annotations.Nullable;

import com.daderpduck.hallucinocraft.blocks.AlembicBlock;
import com.daderpduck.hallucinocraft.blocks.entities.AlembicBlockEntity;

public class AlembicContainer extends BlockEntityContainer<AlembicBlockEntity> implements ButtonHandlerContainer
{
    public static AlembicContainer create(AlembicBlockEntity alembic, Inventory playerInv, int windowId)
    {
        return new AlembicContainer(windowId, alembic).init(playerInv, 12);
    }

    private AlembicContainer(int windowId, AlembicBlockEntity alembic)
    {
        super(ModContainerTypes.ALEMBIC.get(), windowId, alembic);
    }

    @Override
    public void onButtonPress(int buttonID, @Nullable CompoundTag extraNBT)
    {
        Level level = blockEntity.getLevel();
    }

    @Override
    protected void addContainerSlots()
    {
        blockEntity.getCapability(Capabilities.ITEM).ifPresent(inventory -> {
            addSlot(new CallbackSlot(blockEntity, inventory, AlembicBlockEntity.SLOT_FLUID_CONTAINER_IN, 35, 20));
            addSlot(new CallbackSlot(blockEntity, inventory, AlembicBlockEntity.SLOT_FLUID_CONTAINER_OUT, 35, 54));
            addSlot(new CallbackSlot(blockEntity, inventory, AlembicBlockEntity.SLOT_ITEM, 89, 37));
        });
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        final int containerSlot = stack.getCapability(Capabilities.FLUID_ITEM).isPresent() && stack.getCapability(HeatCapability.CAPABILITY).map(cap -> cap.getTemperature() == 0f).orElse(false) ? AlembicBlockEntity.SLOT_FLUID_CONTAINER_IN : AlembicBlockEntity.SLOT_ITEM;

        return switch (typeOf(slotIndex))
            {
                case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, containerSlot, containerSlot + 1, false);
                case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
            };
    }
}
