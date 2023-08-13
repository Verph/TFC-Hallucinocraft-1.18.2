package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.util.climate.ClimateRange;

public class HCDeadClimbingCropBlock extends HCDeadDoubleCropBlock
{
    public static final BooleanProperty STICK = TFCBlockStateProperties.STICK;

    public HCDeadClimbingCropBlock(ExtendedProperties properties, HCCrop crop)
    {
        this(properties, crop.getClimateRange());
    }

    public HCDeadClimbingCropBlock(ExtendedProperties properties, Supplier<ClimateRange> range)
    {
        super(properties, range);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(STICK));
    }
}