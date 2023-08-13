package com.daderpduck.hallucinocraft.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.util.calendar.ICalendarTickable;

import com.daderpduck.hallucinocraft.blocks.crop.HCDoubleCropBlock;

public class HCCropBlockEntity extends CropBlockEntity
{
    public HCCropBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    protected HCCropBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return ModBlockEntities.CROP.get();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        crop.checkForCalendarUpdate();
    }

    public static void serverTickBottomPartOnly(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        if (state.getValue(HCDoubleCropBlock.HC_PART) == HCDoubleCropBlock.Part.BOTTOM)
        {
            crop.checkForCalendarUpdate();
        }
    }
}
