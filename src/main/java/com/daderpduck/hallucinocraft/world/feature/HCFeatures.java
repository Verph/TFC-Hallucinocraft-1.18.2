package com.daderpduck.hallucinocraft.world.feature;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.world.feature.plant.HCTallWildCropFeature;

@SuppressWarnings("unused")
public class HCFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Hallucinocraft.MOD_ID);

    public static final RegistryObject<HCTallWildCropFeature> TALL_WILD_CROP = register("tall_wild_crop", HCTallWildCropFeature::new, HCTallWildCropFeature.CODEC);
    
    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }
}
