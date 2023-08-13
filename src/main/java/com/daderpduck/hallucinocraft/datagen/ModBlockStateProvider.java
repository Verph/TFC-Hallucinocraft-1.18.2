package com.daderpduck.hallucinocraft.datagen;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
    }

    protected void makeCrop(CropBlock block, String modelName, String textureName) {
        getVariantBuilder(block).forAllStates(blockState -> {
            ConfiguredModel[] models = new ConfiguredModel[1];
            models[0] = new ConfiguredModel(models().crop(modelName + blockState.getValue(block.getAgeProperty()),
                    new ResourceLocation(Hallucinocraft.MOD_ID, "block/" + textureName + blockState.getValue(block.getAgeProperty()))));
            return models;
        });
    }

    protected void makeCross(CropBlock block, String modelName, String textureName) {
        getVariantBuilder(block).forAllStates(blockState -> {
            ConfiguredModel[] models = new ConfiguredModel[1];
            models[0] = new ConfiguredModel(models().cross(modelName + blockState.getValue(block.getAgeProperty()),
                    new ResourceLocation(Hallucinocraft.MOD_ID, "block/" + textureName + blockState.getValue(block.getAgeProperty()))));
            return models;
        });
    }


}
