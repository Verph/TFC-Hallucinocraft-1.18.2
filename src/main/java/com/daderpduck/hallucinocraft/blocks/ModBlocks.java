package com.daderpduck.hallucinocraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.common.blocks.ExtendedProperties;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.crop.HCCrop;
import com.daderpduck.hallucinocraft.blocks.entities.AlembicBlockEntity;
import com.daderpduck.hallucinocraft.blocks.entities.CondenserBlockEntity;
import com.daderpduck.hallucinocraft.blocks.entities.ModBlockEntities;
import com.daderpduck.hallucinocraft.fluid.HCAlcohol;
import com.daderpduck.hallucinocraft.fluid.HCSimpleFluid;
import com.daderpduck.hallucinocraft.fluid.ModFluids;
import com.daderpduck.hallucinocraft.items.ModItems;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hallucinocraft.MOD_ID);

    public static final Map<HCCrop, RegistryObject<Block>> CROPS = Helpers.mapOfKeys(HCCrop.class, crop ->
        register("crop/" + crop.name(), crop::create)
    );

    public static final Map<HCCrop, RegistryObject<Block>> DEAD_CROPS = Helpers.mapOfKeys(HCCrop.class, crop ->
        register("dead_crop/" + crop.name(), crop::createDead)
    );

    public static final Map<HCCrop, RegistryObject<Block>> WILD_CROPS = Helpers.mapOfKeys(HCCrop.class, crop ->
        register("wild_crop/" + crop.name(), crop::createWild, Hallucinocraft.TAB)
    );

    public static final RegistryObject<CokeCakeBlock> COKE_CAKE_BLOCK = register("coke_cake", () -> new CokeCakeBlock(BlockBehaviour.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL)));
    public static final RegistryObject<CutPoppyBlock> CUT_POPPY_BLOCK = register("cut_poppy", () -> new CutPoppyBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().noCollission().randomTicks().sound(SoundType.GRASS)));
    public static final RegistryObject<FermentingBottleBlock> FERMENTING_BOTTLE_BLOCK = register("fermenting_bottle", () -> new FermentingBottleBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).noOcclusion().isRedstoneConductor(ModBlocks::never).isViewBlocking(ModBlocks::never).isSuffocating(ModBlocks::never)));

    public static final RegistryObject<AlembicBlock> ALEMBICS = register("devices/alembic", () -> new AlembicBlock(ExtendedProperties.of(Material.METAL).sound(SoundType.NETHERITE_BLOCK).strength(3f).blockEntity(ModBlockEntities.ALEMBIC).serverTicks(AlembicBlockEntity::serverTick)), Hallucinocraft.TAB);
    public static final RegistryObject<CondenserBlock> CONDENSER = register("devices/condenser", () -> new CondenserBlock(ExtendedProperties.of(Material.METAL).sound(SoundType.NETHERITE_BLOCK).strength(3f).blockEntity(ModBlockEntities.CONDENSER).serverTicks(CondenserBlockEntity::serverTick)), Hallucinocraft.TAB);

    public static final Map<HCSimpleFluid, RegistryObject<LiquidBlock>> SIMPLE_FLUIDS = Helpers.mapOfKeys(HCSimpleFluid.class, fluid ->
        register("fluid/" + fluid.getId().toLowerCase(Locale.ROOT), () -> new LiquidBlock(ModFluids.SIMPLE_FLUIDS.get(fluid).source(), Properties.of(Material.WATER).noCollission().strength(100f).noDrops()))
    );

    public static final Map<HCAlcohol, RegistryObject<LiquidBlock>> ALCOHOLS = Helpers.mapOfKeys(HCAlcohol.class, fluid ->
        register("fluid/" + fluid.getId().toLowerCase(Locale.ROOT), () -> new LiquidBlock(ModFluids.ALCOHOLS.get(fluid).source(), Properties.of(Material.WATER).noCollission().strength(100f).noDrops()))
    );

    public static boolean always(BlockState state, BlockGetter level, BlockPos pos)
    {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos)
    {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

    public static boolean onlyColdMobs(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type)
    {
        return true;
        //return Helpers.isEntity(type, TFCTags.Entities.SPAWNS_ON_COLD_BLOCKS);
    }

    private static ToIntFunction<BlockState> alwaysLit()
    {
        return s -> 15;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue)
    {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, CreativeModeTab group)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties().tab(group)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(ModBlocks.BLOCKS, ModItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}
