package com.daderpduck.hallucinocraft.recipe;

import java.util.function.Supplier;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.daderpduck.hallucinocraft.Hallucinocraft;

@SuppressWarnings("unused")
public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Hallucinocraft.MOD_ID);

    public static final RegistryObject<RecipeSerializer<FermentingBottleRecipe>> FERMENTING_BOTTLE_SERIALIZER = register("fermenting_bottle", () -> FermentingBottleRecipe.Serializer.INSTANCE);
    public static final RegistryObject<AlembicRecipe.Serializer> ALEMBIC_SERIALIZER = register("distillation", AlembicRecipe.Serializer::new);

    private static <S extends RecipeSerializer<?>> RegistryObject<S> register(String name, Supplier<S> factory)
    {
        return RECIPE_SERIALIZERS.register(name, factory);
    }
}