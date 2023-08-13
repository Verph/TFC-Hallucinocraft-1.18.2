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

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.AlembicBlock;
import com.daderpduck.hallucinocraft.container.AlembicContainer;
import com.daderpduck.hallucinocraft.recipe.AlembicRecipe;
import com.daderpduck.hallucinocraft.recipe.ModRecipes;
import com.daderpduck.hallucinocraft.util.HCHelpers;


public class AlembicBlockEntity extends TickableInventoryBlockEntity<AlembicBlockEntity.AlembicInventory> implements ICalendarTickable, BarrelInventoryCallback
{
    public static final int SLOT_FLUID_CONTAINER_IN = 0;
    public static final int SLOT_FLUID_CONTAINER_OUT = 1;
    public static final int SLOT_ITEM = 2;
    public static final int SLOTS = 3;

    private static final Component NAME = Helpers.translatable("hallucinocraft.block_entity.alembic");

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlembicBlockEntity alembic)
    {
        // Must run before checkForCalendarUpdate(), as this sets the current recipe.
        if (alembic.recipeName != null)
        {
            alembic.recipe = level.getRecipeManager().byKey(alembic.recipeName)
                .map(b -> b instanceof AlembicRecipe r ? r : null)
                .orElse(null);
                alembic.recipeName = null;
        }

        alembic.checkForLastTickSync();
        alembic.checkForCalendarUpdate();

        if (level.getGameTime() % 5 == 0)
            alembic.updateFluidIOSlots();

        if (!state.getValue(AlembicBlock.CONNECTED))
            return;

        final AlembicRecipe recipe = alembic.recipe;
        if (recipe != null)
        {
            final int duration = (int) (Calendars.SERVER.getTicks() - alembic.recipeTick);
            if (!recipe.isInfinite() && duration > recipe.getDuration())
            {
                if (recipe.matches(alembic.inventory, level))
                {
                    // Recipe completed, so fill outputs
                    //Hallucinocraft.LOGGER.debug("Recipe done, producing output");
                    recipe.assembleOutputs(alembic.inventory);
                    Helpers.playSound(level, alembic.getBlockPos(), recipe.getCompleteSound());
                }

                // In both cases, update the recipe and sync
                alembic.updateRecipe();
                alembic.markForSync();
            }
        }

        if (alembic.needsInstantRecipeUpdate)
        {
            alembic.needsInstantRecipeUpdate = false;
            final RecipeManager recipeManager = level.getRecipeManager();
            Optional.<AlembicRecipe>empty() // For type erasure
                .or(() -> recipeManager.getRecipeFor(ModRecipes.ALEMBIC.get(), alembic.inventory, level))
                .ifPresent(instantRecipe -> {
                    instantRecipe.assembleOutputs(alembic.inventory);
                    if (alembic.soundCooldownTicks == 0)
                    {
                        Helpers.playSound(level, alembic.getBlockPos(), instantRecipe.getCompleteSound());
                        alembic.soundCooldownTicks = 5;
                    }
                });
                alembic.markForSync();
        }

        if (alembic.soundCooldownTicks > 0)
            alembic.soundCooldownTicks--;
    }

    private final SidedHandler.Builder<IFluidHandler> sidedFluidInventory;

    @Nullable private ResourceLocation recipeName;
    @Nullable private AlembicRecipe recipe;
    private long lastUpdateTick = Integer.MIN_VALUE; // The last tick this barrel was updated in serverTick()
    private long recipeTick; // The tick this barrel started working on the current recipe
    private int soundCooldownTicks = 0;

    private boolean needsInstantRecipeUpdate; // If the instant recipe needs to be checked again

    public AlembicBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.ALEMBIC.get(), pos, state, AlembicInventory::new, NAME);

        sidedFluidInventory = new SidedHandler.Builder<>(inventory);

        if (TFCConfig.SERVER.barrelEnableAutomation.get())
        {
            sidedInventory
                .on(new PartialItemHandler(inventory).insert(SLOT_FLUID_CONTAINER_IN).extract(SLOT_FLUID_CONTAINER_OUT), Direction.Plane.HORIZONTAL)
                .on(new PartialItemHandler(inventory).insert(SLOT_ITEM), Direction.UP)
                .on(new PartialItemHandler(inventory).extract(SLOT_ITEM), Direction.DOWN);

            sidedFluidInventory
                .on(new PartialFluidHandler(inventory).insert(), Direction.UP)
                .on(new PartialFluidHandler(inventory).extract(), Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player)
    {
        return AlembicContainer.create(this, player.getInventory(), containerId);
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
        needsInstantRecipeUpdate = true;
        updateRecipe();
    }

    @Override
    public void fluidTankChanged()
    {
        needsInstantRecipeUpdate = true;
        updateRecipe();
        setChanged();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return switch (slot)
            {
                case SLOT_FLUID_CONTAINER_IN -> stack.getCapability(Capabilities.FLUID).isPresent() || stack.getItem() instanceof BucketItem;
                case SLOT_ITEM -> {
                    // We only want to deny heavy/huge (aka things that can hold inventory).
                    // Other than that, barrels don't need a size restriction, and should in general be unrestricted, so we can allow any kind of recipe input (i.e. unfired large vessel)
                    final IItemSize size = ItemSizeManager.get(stack);
                    yield size.getSize(stack).isSmallerThan(Size.HUGE) || size.getWeight(stack).isSmallerThan(Weight.VERY_HEAVY);
                }
                default -> true;
            };
    }

    @Override
    public void onCalendarUpdate(long ticks)
    {
        assert level != null;

        updateRecipe();
        if (recipe == null || recipe.isInfinite())
        {
            return; // No simulation occurs if we were not sealed, or if we had no recipe, or if we had an infinite recipe.
        }

        // Otherwise, begin simulation by jumping to the end tick of the current recipe. If that was in the past, we simulate and retry.
        final long currentTick = Calendars.SERVER.getTicks();
        long lastKnownTick = recipeTick + recipe.getDuration();
        while (lastKnownTick < currentTick)
        {
            // Need to run the recipe completion, as it occurred in the past
            final long offset = currentTick - lastKnownTick;
            assert offset >= 0; // This event should be in the past

            try (CalendarTransaction tr = Calendars.SERVER.transaction())
            {
                tr.add(-offset);

                final AlembicRecipe recipe = this.recipe;
                if (recipe.matches(inventory, null) && getBlockState().getValue(AlembicBlock.CONNECTED))
                {
                    recipe.assembleOutputs(inventory);
                }
                updateRecipe();
                markForSync();
            }

            // Re-check the recipe. If we have an invalid or infinite recipe, then exit simulation. Otherwise, jump forward to the next recipe completion
            // This handles the case where multiple sequential recipes, such as brining -> pickling -> vinegar preservation would've occurred.
            final AlembicRecipe knownRecipe = recipe;
            if (knownRecipe == null)
            {
                return;
            }
            if (knownRecipe.isInfinite())
            {
                return; // No more simulation can occur
            }
            lastKnownTick += recipe.getDuration();
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt)
    {
        nbt.putLong("lastUpdateTick", lastUpdateTick);
        nbt.putLong("recipeTick", recipeTick);
        if (recipe != null)
        {
            // Recipe saved to sync to client
            nbt.putString("recipe", recipe.getId().toString());
        }
        else if (recipeName != null)
        {
            nbt.putString("recipeName", recipeName.toString());
        }
        super.saveAdditional(nbt);
    }

    @Override
    public void loadAdditional(CompoundTag nbt)
    {
        lastUpdateTick = nbt.getLong("lastUpdateTick");
        recipeTick = nbt.getLong("recipeTick");

        recipe = null;
        recipeName = null;
        if (nbt.contains("recipe", Tag.TAG_STRING))
        {
            recipeName = new ResourceLocation(nbt.getString("recipe"));
            if (level != null)
            {
                recipe = level.getRecipeManager().byKey(recipeName)
                    .map(b -> b instanceof AlembicRecipe r ? r : null)
                    .orElse(null);
            }
        }
        super.loadAdditional(nbt);
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
    public void ejectInventory()
    {
        super.ejectInventory();
        assert level != null;
    }

    @Override
    public boolean canModify()
    {
        return true;
    }

    protected void updateRecipe()
    {
        assert level != null;

        final AlembicRecipe oldRecipe = recipe;
        recipe = level.getRecipeManager().getRecipeFor(ModRecipes.ALEMBIC.get(), inventory, level).orElse(null);
        if (recipe != null && oldRecipe != recipe && (oldRecipe == null || !oldRecipe.getId().equals(recipe.getId())))
        {
            // The recipe has changed to a new one, so update the recipe ticks
            recipeTick = Calendars.get(level).getTicks();
            markForSync();
        }

    }

    private void updateFluidIOSlots()
    {
        assert level != null;
        final ItemStack input = inventory.getStackInSlot(SLOT_FLUID_CONTAINER_IN);
        if (!input.isEmpty() && inventory.getStackInSlot(SLOT_FLUID_CONTAINER_OUT).isEmpty())
        {
            FluidHelpers.transferBetweenBlockEntityAndItem(input, this, level, worldPosition, (newOriginalStack, newContainerStack) -> {
                inventory.setStackInSlot(SLOT_FLUID_CONTAINER_IN, newContainerStack);
                inventory.setStackInSlot(SLOT_FLUID_CONTAINER_OUT, newOriginalStack); // Original stack gets shoved in the output
            });
        }
    }

    @Nullable
    public AlembicRecipe getRecipe()
    {
        return recipe;
    }

    public static class AlembicInventory implements DelegateItemHandler, DelegateFluidHandler, INBTSerializable<CompoundTag>, EmptyInventory, FluidTankCallback
    {
        private final BarrelInventoryCallback callback;
        private final InventoryItemHandler inventory;
        private final InventoryFluidTank tank;
        private boolean mutable;

        AlembicInventory(InventoryBlockEntity<?> entity)
        {
            this((BarrelInventoryCallback) entity);
        }

        public AlembicInventory(BarrelInventoryCallback callback)
        {
            this.callback = callback;
            inventory = new InventoryItemHandler(callback, SLOTS);
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

        public boolean isInventoryEmpty()
        {
            return tank.getFluid().isEmpty() && Helpers.isEmpty(inventory);
        }

        public void insertItem(ItemStack stack)
        {
            inventory.insertItem(SLOT_ITEM, stack, false);
        }

        @Override
        public IItemHandlerModifiable getItemHandler()
        {
            return inventory;
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

        @NotNull
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            return canModify() ? inventory.insertItem(slot, stack, simulate) : stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return canModify() ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return canModify() && DelegateItemHandler.super.isItemValid(slot, stack);
        }

        @Override
        public CompoundTag serializeNBT()
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.put("inventory", inventory.serializeNBT());
            nbt.put("tank", tank.writeToNBT(new CompoundTag()));

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt)
        {
            inventory.deserializeNBT(nbt.getCompound("inventory"));
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
