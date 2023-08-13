package com.daderpduck.hallucinocraft.blocks.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;

public class HCFloodedWildCropBlock extends WildCropBlock implements IFluidLoggable
{
    public HCFloodedWildCropBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    public FluidProperty getFluidProperty()
    {
        return TFCBlockStateProperties.FRESH_WATER;
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state)
    {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        // Flooded wild crops need to have a non-fluid block above.
        final BlockPos above = pos.above();
        return super.canSurvive(state, level, pos) && level.getFluidState(above).isEmpty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(getFluidProperty()));
    }
}
