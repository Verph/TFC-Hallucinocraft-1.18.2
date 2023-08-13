package com.daderpduck.hallucinocraft.datagen;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.items.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
    }

    protected void simpleItem(Item item)
    {
        withExistingParent(getItemName(item), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/" + getItemName(item)));
    }

    protected void simpleBlock(Item item, Block block)
    {
        withExistingParent(getItemName(item), new ResourceLocation(Objects.requireNonNull(block.getRegistryName()).getNamespace(), "block/" + Objects.requireNonNull(block.getRegistryName()).getPath()));
    }

    protected void syringe(Item item)
    {
        withExistingParent(getItemName(item), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/syringe_overlay"))
                .texture("layer1", new ResourceLocation(Hallucinocraft.MOD_ID, "item/syringe"));
    }

    protected static String getItemName(Item item)
    {
        return Objects.requireNonNull(item.getRegistryName()).getPath();
    }
}
