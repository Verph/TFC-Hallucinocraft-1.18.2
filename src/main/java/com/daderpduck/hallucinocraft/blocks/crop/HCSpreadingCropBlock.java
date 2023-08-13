package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.function.Supplier;

import com.daderpduck.hallucinocraft.blocks.HCBlockStateProperties;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.items.ModItems;
import com.daderpduck.hallucinocraft.util.climate.HCClimateRanges;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;

public abstract class HCSpreadingCropBlock extends HCDefaultCropBlock
{
    public static HCSpreadingCropBlock create(ExtendedProperties properties, int stages, HCCrop crop, Supplier<Supplier<? extends Block>> fruit, Supplier<? extends Item> productItem)
    {
        final IntegerProperty property = HCBlockStateProperties.getAgeProperty(stages - 1);
        return new HCSpreadingCropBlock(properties, stages - 1, ModBlocks.DEAD_CROPS.get(crop), ModItems.CROP_SEEDS.get(crop), crop.getPrimaryNutrient(), HCClimateRanges.CROPS.get(crop), fruit, productItem, crop)
        {
            @Override
            public IntegerProperty getAgeProperty()
            {
                return property;
            }
        };
    }

    private final Supplier<Supplier<? extends Block>> fruit;

    protected HCSpreadingCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange, Supplier<Supplier<? extends Block>> fruit, Supplier<? extends Item> productItem, HCCrop crop)
    {
        super(properties, maxAge, dead, seeds, primaryNutrient, climateRange, productItem, crop);
        this.fruit = fruit;
    }

    @Override
    public float getGrowthLimit(Level level, BlockPos pos, BlockState state)
    {
        int fruitAround = 0;
        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        final Block fruit = getFruit();
        for (Direction d : Direction.Plane.HORIZONTAL)
        {
            mutable.setWithOffset(pos, d);
            BlockState offsetState = level.getBlockState(mutable);
            if (Helpers.isBlock(offsetState, fruit))
            {
                fruitAround++;
                if (fruitAround > 2)
                {
                    return 0.9f;
                }
            }
        }
        return super.getGrowthLimit(level, pos, state);
    }

    @Override
    protected void postGrowthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        super.postGrowthTick(level, pos, state, crop);
        if (crop.getGrowth() >= 1)
        {
            final Direction offset = Direction.Plane.HORIZONTAL.getRandomDirection(level.getRandom());
            final BlockPos fruitPos = pos.relative(offset);
            final Block fruitBlock = getFruit();
            final BlockState fruitState = fruitBlock.defaultBlockState();
            final BlockState growingOn = level.getBlockState(fruitPos.below());
            if (Helpers.isBlock(growingOn, TFCTags.Blocks.SPREADING_FRUIT_GROWS_ON) && level.getBlockState(fruitPos).getMaterial().isReplaceable())
            {
                level.setBlockAndUpdate(fruitPos, fruitState);
                if (level.getBlockEntity(fruitPos) instanceof DecayingBlockEntity decaying)
                {
                    decaying.setStack(new ItemStack(fruitBlock));
                }
                crop.setGrowth(Mth.nextFloat(level.getRandom(), 0.8f, 0.87f));
            }
        }
    }

    public Block getFruit()
    {
        return fruit.get().get();
    }
}
