package com.daderpduck.hallucinocraft.datagen;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.datagen.custom.NbtShapedRecipeBuilder;
import com.daderpduck.hallucinocraft.items.ModItems;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder
{
    public ModRecipeProvider(DataGenerator pGenerator)
    {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
    {
    }

    protected static void syringe(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike result, ItemLike ingredient)
    {
        ShapelessRecipeBuilder.shapeless(result)
                .requires(ingredient)
                .requires(ModItems.EMPTY_SYRINGE.get())
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(finishedRecipeConsumer);
    }

    protected static void campfireCooking(Consumer<FinishedRecipe> finishedRecipeConsumer, Item ingredient, Item result)
    {
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, 0.35F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(finishedRecipeConsumer, new ResourceLocation(Hallucinocraft.MOD_ID,getItemName(result) + "_from_campfire_cooking"));
    }
}
