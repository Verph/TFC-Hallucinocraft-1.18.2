package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import org.jetbrains.annotations.Nullable;

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

public abstract class HCDoubleCropBlock extends CropBlock
{
    public static final EnumProperty<Part> HC_PART = HCBlockStateProperties.DOUBLE_CROP_PART;

    public static HCDoubleCropBlock create(ExtendedProperties properties, int singleStages, int doubleStages, HCCrop crop, Supplier<? extends Item> productItem)
    {
        final IntegerProperty property = HCBlockStateProperties.getAgeProperty(singleStages + doubleStages - 1);
        return new HCDoubleCropBlock(properties, singleStages - 1, singleStages + doubleStages - 1, ModBlocks.DEAD_CROPS.get(crop), ModItems.CROP_SEEDS.get(crop), crop.getPrimaryNutrient(), HCClimateRanges.CROPS.get(crop), productItem, crop)
        {
            @Override
            public IntegerProperty getAgeProperty()
            {
                return property;
            }
        };
    }

    public final HCCrop crop;
    public final int maxSingleAge;
    public final float maxSingleGrowth;
    public final Supplier<? extends Item> productItem;

    protected HCDoubleCropBlock(ExtendedProperties properties, int maxSingleAge, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange, Supplier<? extends Item> productItem, HCCrop crop)
    {
        super(properties, maxAge, dead, seeds, primaryNutrient, climateRange);

        this.maxSingleAge = maxSingleAge;
        this.maxSingleGrowth = (float) maxSingleAge / maxAge;
        this.productItem = productItem;
        this.crop = crop;
        registerDefaultState(defaultBlockState().setValue(getAgeProperty(), 0).setValue(HC_PART, Part.BOTTOM));
    }

    @Override
    public abstract IntegerProperty getAgeProperty();

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        final Part part = state.getValue(HC_PART);
        final BlockState belowState = level.getBlockState(pos.below());
        if (part == Part.BOTTOM)
        {
            return Helpers.isBlock(belowState.getBlock(), TFCTags.Blocks.FARMLAND);
        }
        else
        {
            return Helpers.isBlock(belowState, this) && belowState.getValue(HC_PART) == Part.BOTTOM;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        HCDoubleCropBlock.Part part = state.getValue(HC_PART);
        if (facing.getAxis() != Direction.Axis.Y || part == HCDoubleCropBlock.Part.BOTTOM != (facing == Direction.UP) || facingState.getBlock() == this && facingState.getValue(HC_PART) != part)
        {
            return part == HCDoubleCropBlock.Part.BOTTOM && facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
        else
        {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        HCWildDoubleCropBlock.onPlayerWillDestroy(level, pos, state, player);
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity tile, ItemStack stack)
    {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), tile, stack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(getAgeProperty(), HC_PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (state.getValue(HC_PART) == Part.TOP) return HALF_SHAPE;
        float growth = (float) state.getValue(getAgeProperty()) / maxSingleAge;
        if (growth <= 0.25F) return QUARTER_SHAPE;
        else if (growth <= 0.5F) return HALF_SHAPE;
        return FULL_SHAPE;
    }

    @Override
    public void growthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        // Only perform growth ticks on the lower part
        if (state.getValue(HCDoubleCropBlock.HC_PART) == Part.BOTTOM)
        {
            super.growthTick(level, pos, state, crop);
        }
    }

    @Override
    protected void postGrowthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        final BlockPos posAbove = pos.above();
        final BlockState stateAbove = level.getBlockState(posAbove);
        final int age = Mth.clamp((int) (crop.getGrowth() * getMaxAge()), 0, getMaxAge());

        level.setBlock(pos, state.setValue(getAgeProperty(), age), Block.UPDATE_CLIENTS);
        if (age > maxSingleAge && (stateAbove.isAir() || stateAbove.getBlock() == this))
        {
            level.setBlock(posAbove, state.setValue(getAgeProperty(), age).setValue(HC_PART, Part.TOP), Block.UPDATE_ALL);
        }
    }

    @Override
    public float getGrowthLimit(Level level, BlockPos pos, BlockState state)
    {
        return isSameOrAir(level.getBlockState(pos.above())) ? super.getGrowthLimit(level, pos, state) : maxSingleGrowth;
    }

    @Override
    public void die(Level level, BlockPos pos, BlockState state, boolean fullyGrown)
    {
        final BlockPos posAbove = pos.above();
        final BlockState stateAbove = level.getBlockState(posAbove);
        final BlockState deadState = dead.get().defaultBlockState().setValue(HCDeadCropBlock.MATURE, fullyGrown);
        if (fullyGrown && isSameOrAir(stateAbove))
        {
            level.setBlock(posAbove, deadState.setValue(HCDeadDoubleCropBlock.HC_PART, Part.TOP), Block.UPDATE_CLIENTS);
        }
        else if (stateAbove.getBlock() == this)
        {
            level.destroyBlock(posAbove, false);
        }
        level.setBlockAndUpdate(pos, deadState.setValue(HCDeadDoubleCropBlock.HC_PART, Part.BOTTOM));
    }

    protected boolean isSameOrAir(BlockState state)
    {
        return state.isAir() || state.getBlock() == this;
    }

    @Override
    public void addHoeOverlayInfo(Level level, BlockPos pos, BlockState state, List<Component> text, boolean isDebug)
    {
        super.addHoeOverlayInfo(level, state.getValue(HC_PART) == Part.TOP ? pos.below() : pos, state, text, isDebug);
    }

    public enum Part implements StringRepresentable
    {
        BOTTOM, TOP;

        private final String serializedName;

        Part()
        {
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        super.use(state, level, pos, player, hand, hit);
        if (state.getValue(getAgeProperty()) == getMaxAge() && productItem != null)
        {
            level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS, 1.0f, level.getRandom().nextFloat() + 0.7f + 0.3f);
            if (!level.isClientSide())
            {
                ItemHandlerHelper.giveItemToPlayer(player, getProductItem(level.getRandom()));
                final BlockPos posAbove = pos.above();
                final BlockState stateAbove = level.getBlockState(posAbove);
                final BlockPos posBelow = pos.below();
                final BlockState stateBelow = level.getBlockState(posBelow);

                int ageAfterPicking = Mth.clamp(Math.round((getMaxAge() * 0.7F)) - 1, 0, getMaxAge());

                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(getAgeProperty(), ageAfterPicking));
                if (stateAbove.getBlock() == ModBlocks.CROPS.get(crop).get())
                {
                    level.setBlockAndUpdate(posAbove, level.getBlockState(posAbove).setValue(getAgeProperty(), ageAfterPicking));
                }
                else if (stateBelow.getBlock() == ModBlocks.CROPS.get(crop).get())
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