package com.daderpduck.hallucinocraft.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import net.dries007.tfc.common.recipes.ISimpleRecipe;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.common.recipes.ingredients.ItemStackIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.entities.AlembicBlockEntity;

public class AlembicRecipe implements ISimpleRecipe<AlembicBlockEntity.AlembicInventory>
{
    private final ResourceLocation id;

    protected final ItemStackIngredient inputItem;
    protected final FluidStackIngredient inputFluid;
    protected final ItemStackProvider outputItem;
    protected final FluidStack outputFluid;
    protected final SoundEvent sound;
    private final int duration;

    public AlembicRecipe(ResourceLocation id, Builder builder, int duration)
    {
        this.id = id;
        this.inputItem = builder.inputItem;
        this.inputFluid = builder.inputFluid;
        this.outputItem = builder.outputItem;
        this.outputFluid = builder.outputFluid;
        this.sound = builder.sound;
        this.duration = duration;
    }

    public void assembleOutputs(AlembicBlockEntity.AlembicInventory inventory)
    {
        // Require the inventory to be mutable, as we use insert/extract methods, but will expect it to be modifiable despite being sealed.
        inventory.whileMutable(() -> {
            // Remove all inputs
            final ItemStack stack = inventory.extractItem(AlembicBlockEntity.SLOT_ITEM, inputItem.count(), false);
            //final ItemStack stack = Helpers.removeStack(inventory, AlembicBlockEntity.SLOT_ITEM);
            final FluidStack fluid = inventory.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);

            // Output items
            // All output items, and then remaining input items, get inserted into the output overflow
            final ItemStack outputItem = this.outputItem.getSingleStack(stack);
            if (!outputItem.isEmpty())
            {
                insertItemStackSize(outputItem, inputItem.count(), inventory::insertItem);
            }

            // Output fluid
            // If there's no output fluid, keep as much of the input as possible
            // If there is an output fluid, excess input is voided
            final FluidStack outputFluid = this.outputFluid.copy();

            outputFluid.setAmount(inputFluid.amount());
            inventory.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
            
            /*fluid.setAmount(fluid.getAmount() - inputFluid.amount());
            inventory.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);*/
        });
    }

    public static void insertItemStackSize(ItemStack stack, int totalCount, Consumer<ItemStack> consumer)
    {
        final ItemStack outStack = stack.copy();
        outStack.setCount(totalCount);
        consumer.accept(outStack);
    }

    @Override
    public boolean matches(AlembicBlockEntity.AlembicInventory container, @Nullable Level level)
    {
        return inputItem.test(container.getStackInSlot(AlembicBlockEntity.SLOT_ITEM)) && inputFluid.test(container.getFluidInTank(0));
    }

    @Override
    public ItemStack getResultItem()
    {
        return outputItem.getEmptyStack();
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.ALEMBIC_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return ModRecipes.ALEMBIC.get();
    }

    public ItemStackIngredient getInputItem()
    {
        return inputItem;
    }

    public ItemStackProvider getOutputItem()
    {
        return outputItem;
    }

    public FluidStackIngredient getInputFluid()
    {
        return inputFluid;
    }

    public FluidStack getOutputFluid()
    {
        return outputFluid;
    }

    public SoundEvent getCompleteSound()
    {
        return sound;
    }

    public int getDuration()
    {
        return duration;
    }

    public boolean isInfinite()
    {
        return duration <= 0;
    }

    public TranslatableComponent getTranslationComponent()
    {
        return Helpers.translatable("tfc.recipe.barrel." + id.getNamespace() + "." + id.getPath().replace('/', '.'));
    }

    public record Builder(ItemStackIngredient inputItem, FluidStackIngredient inputFluid, ItemStackProvider outputItem, FluidStack outputFluid, SoundEvent sound)
    {
        public static Builder fromJson(JsonObject json)
        {
            final ItemStackIngredient inputItem = json.has("input_item") ? ItemStackIngredient.fromJson(JsonHelpers.getAsJsonObject(json, "input_item")) : ItemStackIngredient.EMPTY;
            final FluidStackIngredient inputFluid = json.has("input_fluid") ? FluidStackIngredient.fromJson(JsonHelpers.getAsJsonObject(json, "input_fluid")) : FluidStackIngredient.EMPTY;

            if (inputFluid == FluidStackIngredient.EMPTY)
            {
                throw new JsonParseException("Alembic recipe must have an input_fluid");
            }

            final ItemStackProvider outputItem = json.has("output_item") ? ItemStackProvider.fromJson(JsonHelpers.getAsJsonObject(json, "output_item")) : ItemStackProvider.empty();
            final FluidStack outputFluid = json.has("output_fluid") ? JsonHelpers.getFluidStack(JsonHelpers.getAsJsonObject(json, "output_fluid")) : FluidStack.EMPTY;
            final SoundEvent sound = json.has("sound") ? JsonHelpers.getRegistryEntry(json, "sound", ForgeRegistries.SOUND_EVENTS) : SoundEvents.BREWING_STAND_BREW;

            if (outputFluid == FluidStack.EMPTY)
            {
                throw new JsonParseException("Alembic recipe must have an outputFluid");
            }

            return new Builder(inputItem, inputFluid, outputItem, outputFluid, sound);
        }

        public static Builder fromNetwork(FriendlyByteBuf buffer)
        {
            final ItemStackIngredient inputItem = ItemStackIngredient.fromNetwork(buffer);
            final ItemStackProvider outputItem = ItemStackProvider.fromNetwork(buffer);
            final Builder builder = fromNetworkFluidsOnly(buffer);

            return new Builder(inputItem, builder.inputFluid, outputItem, builder.outputFluid, builder.sound);
        }

        public static void toNetwork(AlembicRecipe recipe, FriendlyByteBuf buffer)
        {
            recipe.inputItem.toNetwork(buffer);
            recipe.outputItem.toNetwork(buffer);
            toNetworkFluidsOnly(recipe, buffer);
        }

        public static Builder fromNetworkFluidsOnly(FriendlyByteBuf buffer)
        {
            final FluidStackIngredient inputFluid = FluidStackIngredient.fromNetwork(buffer);
            final FluidStack outputFluid = FluidStack.readFromPacket(buffer);
            final SoundEvent sound = buffer.readRegistryIdUnsafe(ForgeRegistries.SOUND_EVENTS);

            return new Builder(ItemStackIngredient.EMPTY, inputFluid, ItemStackProvider.empty(), outputFluid, sound);
        }

        public static void toNetworkFluidsOnly(AlembicRecipe recipe, FriendlyByteBuf buffer)
        {
            recipe.inputFluid.toNetwork(buffer);
            recipe.outputFluid.writeToPacket(buffer);
            buffer.writeRegistryIdUnsafe(ForgeRegistries.SOUND_EVENTS, recipe.sound);
        }
    }

    public static class Serializer extends RecipeSerializerImpl<AlembicRecipe>
    {
        @Override
        public AlembicRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            final Builder builder = Builder.fromJson(json);
            final int duration = JsonHelpers.getAsInt(json, "duration");
            return new AlembicRecipe(recipeId, builder, duration);
        }

        @Nullable
        @Override
        public AlembicRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            final Builder builder = Builder.fromNetwork(buffer);
            final int duration = buffer.readVarInt();
            return new AlembicRecipe(recipeId, builder, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlembicRecipe recipe)
        {
            Builder.toNetwork(recipe, buffer);
            buffer.writeVarInt(recipe.duration);
        }
    }
}
