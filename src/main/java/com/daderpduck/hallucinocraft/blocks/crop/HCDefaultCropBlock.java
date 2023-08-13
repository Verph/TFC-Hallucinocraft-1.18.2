package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;

import com.daderpduck.hallucinocraft.HallucinocraftConfig;
import com.daderpduck.hallucinocraft.blocks.HCBlockStateProperties;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.items.ModItems;
import com.daderpduck.hallucinocraft.util.climate.HCClimateRanges;

public abstract class HCDefaultCropBlock extends CropBlock
{
    public static HCDefaultCropBlock create(ExtendedProperties properties, int stages, HCCrop crop, Supplier<? extends Item> productItem)
    {
        final IntegerProperty property = HCBlockStateProperties.getAgeProperty(stages - 1);
        return new HCDefaultCropBlock(properties, stages - 1, ModBlocks.DEAD_CROPS.get(crop), ModItems.CROP_SEEDS.get(crop), crop.getPrimaryNutrient(), HCClimateRanges.CROPS.get(crop), productItem, crop)
        {
            @Override
            public IntegerProperty getAgeProperty()
            {
                return property;
            }
        };
    }

    @Override
    public abstract IntegerProperty getAgeProperty();

    public final HCCrop crop;
    public final Supplier<? extends Item> productItem;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(getAgeProperty());
    }

    protected HCDefaultCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange, Supplier<? extends Item> productItem, HCCrop crop)
    {
        super(properties, maxAge, dead, seeds, primaryNutrient, climateRange);
        this.productItem = productItem;
        this.crop = crop;
        registerDefaultState(defaultBlockState().setValue(getAgeProperty(), 0));
    }

    public int getMaxAgeForDrop()
    {
        return getMaxAge() - 1;
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
    public void die(Level level, BlockPos pos, BlockState state, boolean fullyGrown)
    {
        final BlockState deadState = dead.get().defaultBlockState().setValue(HCDeadCropBlock.MATURE, fullyGrown);
        level.setBlockAndUpdate(pos, deadState);
    }

    @Override
    protected void postGrowthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        final int age = crop.getGrowth() == 1 ? getMaxAge() : (int) (crop.getGrowth() * getMaxAge());
        level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), age));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        super.use(state, level, pos, player, hand, hit);
        if (state.getValue(getAgeProperty()) == getMaxAgeForDrop() && productItem != null)
        {
            level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS, 1.0f, level.getRandom().nextFloat() + 0.7f + 0.3f);
            if (!level.isClientSide())
            {
                ItemHandlerHelper.giveItemToPlayer(player, getProductItem(level.getRandom()));
                final BlockPos posAbove = pos.above();
                final BlockState stateAbove = level.getBlockState(posAbove);
                final BlockPos posBelow = pos.below();
                final BlockState stateBelow = level.getBlockState(posBelow);

                int ageAfterPicking = (int) Mth.clamp((getMaxAgeForDrop() * 0.7F) - 1, 0, getMaxAgeForDrop());

                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(getAgeProperty(), ageAfterPicking));
                if (stateAbove.getBlock() == this)
                {
                    level.setBlockAndUpdate(posAbove, level.getBlockState(posAbove).setValue(getAgeProperty(), ageAfterPicking));
                }
                else if (stateBelow.getBlock() == this)
                {
                    level.setBlockAndUpdate(posBelow, level.getBlockState(posBelow).setValue(getAgeProperty(), ageAfterPicking));
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public ItemStack getProductItem(Random random)
    {
        return new ItemStack(productItem.get(), random.nextInt(HallucinocraftConfig.COMMON.additionalCropDrops.get()) + 1);
    }

    @Override
    public OffsetType getOffsetType()
    {
        return OffsetType.XZ;
    }
}