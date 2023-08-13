package com.daderpduck.hallucinocraft.drugs.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.items.ItemHandlerHelper;

import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.collections.IndirectHashCollection;

import org.jetbrains.annotations.Nullable;

import com.daderpduck.hallucinocraft.util.HCHelpers;

public class DrugCapability
{
    public static final Capability<IDrug> CAPABILITY = HCHelpers.capability(new CapabilityToken<>() {});
    public static final ResourceLocation KEY = HCHelpers.identifier("drugs");
    public static final DataManager<DrugDefinition> MANAGER = new DataManager<>(HCHelpers.identifier("drug_items"), "drugs", DrugDefinition::new, DrugDefinition::new, DrugDefinition::encode, Packet::new);
    public static final IndirectHashCollection<Item, DrugDefinition> CACHE = IndirectHashCollection.create(DrugDefinition::getValidItems, MANAGER::getValues);

    @Nullable
    public static DrugDefinition get(ItemStack stack)
    {
        for (DrugDefinition def : CACHE.getAll(stack.getItem()))
        {
            if (def.matches(stack))
            {
                return def;
            }
        }
        return null;
    }

    /**
     * Merges two item stacks with different creation dates, taking the earlier of the two.
     *
     * @param stackToMergeInto the stack to merge into. Not modified.
     * @param stackToMerge     the stack to merge, which will be left with the remainder after merging. Will be modified.
     * @return The merged stack.
     */
    public static ItemStack mergeItemStacks(ItemStack stackToMergeInto, ItemStack stackToMerge)
    {
        if (stackToMerge.isEmpty())
        {
            return stackToMergeInto;
        }
        else if (stackToMergeInto.isEmpty())
        {
            final ItemStack merged = stackToMerge.copy();
            stackToMerge.setCount(0);
            return merged;
        }
        else if (DrugCapability.areStacksStackableExceptCreationDate(stackToMergeInto, stackToMerge))
        {
            final IDrug mergeIntoFood = stackToMergeInto.getCapability(DrugCapability.CAPABILITY).resolve().orElse(null);
            final IDrug mergeFood = stackToMerge.getCapability(DrugCapability.CAPABILITY).resolve().orElse(null);

            final int mergeAmount = Math.min(stackToMerge.getCount(), stackToMergeInto.getMaxStackSize() - stackToMergeInto.getCount());
            stackToMerge.shrink(mergeAmount);
            stackToMergeInto.grow(mergeAmount);
        }
        return stackToMergeInto;
    }

    /**
     * This is a nice way of checking if two stacks are stackable, ignoring the creation date: copy both stacks, give them the same creation date, then check compatibility
     * This will also not stack stacks which have different traits, which is intended
     *
     * @return true if the stacks are otherwise stackable ignoring their creation date
     */
    public static boolean areStacksStackableExceptCreationDate(ItemStack stack1, ItemStack stack2)
    {
        final ItemStack stack1Copy = stack1.copy(), stack2Copy = stack2.copy();
        return ItemHandlerHelper.canItemStacksStack(stack1Copy, stack2Copy);
    }

    public static class Packet extends DataManagerSyncPacket<DrugDefinition> {}
}
