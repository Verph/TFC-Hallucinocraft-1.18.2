package com.daderpduck.hallucinocraft.blocks.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.plant.TFCBushBlock;
import net.dries007.tfc.util.Helpers;

public class HCWildCropBlock extends TFCBushBlock
{
    public final HCCrop crop;

    public HCWildCropBlock(ExtendedProperties properties, HCCrop crop)
    {
        super(properties);
        this.crop = crop;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return CropBlock.FULL_SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        BlockState belowState = level.getBlockState(pos);
        if (crop == HCCrop.AGAVE || crop == HCCrop.PEYOTE)
            return super.mayPlaceOn(state, level, pos) || Helpers.isBlock(belowState, BlockTags.SAND) || Helpers.isBlock(state.getBlock(), TFCTags.Blocks.GRASS_PLANTABLE_ON) || Helpers.isBlock(state.getBlock(), TFCTags.Blocks.BUSH_PLANTABLE_ON);
        else
            return super.mayPlaceOn(state, level, pos);
    }

    @Override
    public OffsetType getOffsetType()
    {
        return OffsetType.XZ;
    }
}
