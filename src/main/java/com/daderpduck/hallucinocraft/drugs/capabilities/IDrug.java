package com.daderpduck.hallucinocraft.drugs.capabilities;

import java.util.List;

import com.daderpduck.hallucinocraft.drugs.DrugInstance;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

import com.daderpduck.hallucinocraft.util.HCHelpers;

public interface IDrug extends INBTSerializable<CompoundTag>
{
    /**
     * Get a visible measure of all immutable data associated with food
     * - Nutrition information
     * - Hunger / Saturation
     * - Water (Thirst)
     *
     * @see FoodData
     */
    DrugData getData();

    /**
     * Gets the current list of traits on this food
     * Can also be used to add traits to the food
     *
     * @return the traits of the food
     */
    List<DrugInstance> getDrugs();

    /**
     * Tooltip added to the food item
     *
     * @param stack the stack in question
     * @param text  the tooltip
     */
    default void addTooltipInfo(ItemStack stack, List<Component> text)
    {
        // Add info for each drug
        for (DrugInstance drug : getDrugs())
        {
            text.add(HCHelpers.literal("Drug: " + drug.toName()));
        }
    }
}
