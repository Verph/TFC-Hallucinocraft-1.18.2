package com.daderpduck.hallucinocraft.blocks.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.*;
import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.dries007.tfc.common.recipes.SealedBarrelRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.common.recipes.inventory.EmptyInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.CalendarTransaction;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.dries007.tfc.common.blockentities.BarrelInventoryCallback;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CondenserBlockEntity extends TickableInventoryBlockEntity<CondenserBlockEntity.CondenserInventory> implements ICalendarTickable, BarrelInventoryCallback
{
    public static final int SLOT_FLUID_CONTAINER_IN = 0;

    private static final Component NAME = Helpers.translatable("tfc.block_entity.barrel");

    public static void serverTick(Level level, BlockPos pos, BlockState state, CondenserBlockEntity condenser)
    {
    }

    private final SidedHandler.Builder<IFluidHandler> sidedFluidInventory;

    private long lastUpdateTick = Integer.MIN_VALUE; // The last tick this barrel was updated in serverTick()

    public CondenserBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CONDENSER.get(), pos, state, CondenserInventory::new, NAME);

        sidedFluidInventory = new SidedHandler.Builder<>(inventory);

        if (TFCConfig.SERVER.barrelEnableAutomation.get())
        {
            sidedFluidInventory
                .on(new PartialFluidHandler(inventory).insert(), Direction.UP)
                .on(new PartialFluidHandler(inventory).extract(), Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        if (cap == Capabilities.FLUID)
        {
            return sidedFluidInventory.getSidedHandler(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);
    }

    @Override
    public void fluidTankChanged()
    {
        setChanged();
    }

    @Override
    public void onCalendarUpdate(long ticks)
    {
        assert level != null;
    }

    @Override
    @Deprecated
    public long getLastUpdateTick()
    {
        return lastUpdateTick;
    }

    @Override
    @Deprecated
    public void setLastUpdateTick(long tick)
    {
        lastUpdateTick = tick;
    }

    @Override
    public boolean canModify()
    {
        return true;
    }

    public static class CondenserInventory implements DelegateItemHandler, DelegateFluidHandler, INBTSerializable<CompoundTag>, EmptyInventory, FluidTankCallback
    {
        private final BarrelInventoryCallback callback;
        private final InventoryFluidTank tank;
        private boolean mutable; // If the inventory is pretending to be mutable, despite the barrel being sealed and preventing extractions / insertions

        CondenserInventory(InventoryBlockEntity<?> entity)
        {
            this((BarrelInventoryCallback) entity);
        }

        public CondenserInventory(BarrelInventoryCallback callback)
        {
            this.callback = callback;
            tank = new InventoryFluidTank(TFCConfig.SERVER.barrelCapacity.get(), stack -> Helpers.isFluid(stack.getFluid(), TFCTags.Fluids.USABLE_IN_BARREL), this);
        }

        public void whileMutable(Runnable action)
        {
            try
            {
                mutable = true;
                action.run();
            }
            finally
            {
                mutable = false;
            }
        }

        @Override
        public IItemHandlerModifiable getItemHandler()
        {
            return null;
        }

        public boolean isInventoryEmpty()
        {
            return tank.getFluid().isEmpty();
        }

        @Override
        public IFluidHandler getFluidHandler()
        {
            return tank;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return canModify() ? tank.fill(resource, action) : 0;
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            return canModify() ? tank.drain(resource, action) : FluidStack.EMPTY;
        }

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            return canModify() ? tank.drain(maxDrain, action) : FluidStack.EMPTY;
        }

        @Override
        public CompoundTag serializeNBT()
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.put("tank", tank.writeToNBT(new CompoundTag()));

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt)
        {
            tank.readFromNBT(nbt.getCompound("tank"));
        }

        @Override
        public void fluidTankChanged()
        {
            callback.fluidTankChanged();
        }

        private boolean canModify()
        {
            return mutable || callback.canModify();
        }
    }
}
