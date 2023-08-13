package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import com.daderpduck.hallucinocraft.blocks.HCBlockStateProperties;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.items.ModItems;
import com.daderpduck.hallucinocraft.util.climate.HCClimateRanges;

import net.dries007.tfc.client.IGhostBlockHandler;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.CropHelpers;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;

public abstract class HCClimbingCropBlock extends HCDoubleCropBlock implements IGhostBlockHandler
{
    public static final BooleanProperty STICK = TFCBlockStateProperties.STICK;

    public static HCClimbingCropBlock create(ExtendedProperties properties, int singleStages, int doubleStages, HCCrop crop, Supplier<? extends Item> productItem)
    {
        final IntegerProperty property = HCBlockStateProperties.getAgeProperty(singleStages + doubleStages - 1);
        return new HCClimbingCropBlock(properties, singleStages - 1, singleStages + doubleStages - 1, ModBlocks.DEAD_CROPS.get(crop), ModItems.CROP_SEEDS.get(crop), crop.getPrimaryNutrient(), HCClimateRanges.CROPS.get(crop), productItem, crop)
        {
            @Override
            public IntegerProperty getAgeProperty()
            {
                return property;
            }
        };
    }

    protected HCClimbingCropBlock(ExtendedProperties properties, int maxSingleAge, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange, Supplier<? extends Item> productItem, HCCrop crop)
    {
        super(properties, maxSingleAge, maxAge, dead, seeds, primaryNutrient, climateRange, productItem, crop);
        registerDefaultState(getStateDefinition().any().setValue(STICK, false).setValue(HC_PART, Part.BOTTOM));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        super.use(state, level, pos, player, hand, hit);
        final ItemStack heldStack = player.getItemInHand(hand);
        final BlockPos posAbove = pos.above();
        if (Helpers.isItem(heldStack.getItem(), Tags.Items.RODS_WOODEN) && !state.getValue(STICK) && level.isEmptyBlock(posAbove) && posAbove.getY() <= level.getMaxBuildHeight())
        {
            if (!level.isClientSide())
            {
                level.setBlock(pos, state.setValue(STICK, true), Block.UPDATE_CLIENTS);
                level.setBlock(pos.above(), state.setValue(STICK, true).setValue(HC_PART, Part.TOP), Block.UPDATE_ALL);
                if (!player.isCreative()) heldStack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(STICK));
    }

    @Override
    public float getGrowthLimit(Level level, BlockPos pos, BlockState state)
    {
        if (!CropHelpers.lightValid(level, pos))
        {
            return 0f;
        }
        final BlockState stateAbove = level.getBlockState(pos.above());
        return stateAbove.getBlock() == this && stateAbove.getValue(STICK) && stateAbove.getValue(HC_PART) == Part.TOP ? CropHelpers.GROWTH_LIMIT : maxSingleGrowth;
    }

    @Override
    public void die(Level level, BlockPos pos, BlockState state, boolean fullyGrown)
    {
        final BlockPos posAbove = pos.above();
        final BlockState stateAbove = level.getBlockState(posAbove);
        final boolean hasTop = stateAbove.getBlock() == this;
        final BlockState deadState = dead.get().defaultBlockState().setValue(HCDeadCropBlock.MATURE, fullyGrown).setValue(STICK, state.getValue(STICK));
        if (hasTop)
        {
            level.setBlock(posAbove, deadState.setValue(HCDeadDoubleCropBlock.HC_PART, Part.TOP), Block.UPDATE_CLIENTS);
        }
        else
        {
            level.destroyBlock(posAbove, false);
        }
        level.setBlockAndUpdate(pos, deadState.setValue(HCDeadDoubleCropBlock.HC_PART, Part.BOTTOM));
    }

    @Nullable
    @Override
    public BlockState getStateToDraw(Level level, Player player, BlockState state, Direction direction, BlockPos pos, double x, double y, double z, ItemStack item)
    {
        BlockPos abovePos = pos.above();
        if (Helpers.isItem(item.getItem(), Tags.Items.RODS_WOODEN) && !state.getValue(STICK) && level.isEmptyBlock(abovePos) && abovePos.getY() <= level.getMaxBuildHeight())
        {
            return state.setValue(STICK, true);
        }
        return null;
    }
}