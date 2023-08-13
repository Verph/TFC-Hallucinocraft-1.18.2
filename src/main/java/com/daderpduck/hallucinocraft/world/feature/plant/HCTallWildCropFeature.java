package com.daderpduck.hallucinocraft.world.feature.plant;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import com.mojang.serialization.Codec;
import net.dries007.tfc.world.feature.BlockConfig;

import com.daderpduck.hallucinocraft.blocks.crop.HCWildDoubleCropBlock;

public class HCTallWildCropFeature extends Feature<BlockConfig<HCWildDoubleCropBlock>>
{
    public static final Codec<BlockConfig<HCWildDoubleCropBlock>> CODEC = BlockConfig.codec(b -> b instanceof HCWildDoubleCropBlock t ? t : null, "Must be a " + HCWildDoubleCropBlock.class.getSimpleName());

    public HCTallWildCropFeature(Codec<BlockConfig<HCWildDoubleCropBlock>> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockConfig<HCWildDoubleCropBlock>> context)
    {
        context.config().block().placeTwoHalves(context.level(), context.origin(), 2);
        return true;
    }
}
