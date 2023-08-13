package com.daderpduck.hallucinocraft.blocks.entities;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Hallucinocraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<HCCropBlockEntity>> CROP = register("crop", HCCropBlockEntity::new, ModBlocks.CROPS.values().stream());
    public static final RegistryObject<BlockEntityType<FermentingBottleBlockEntity>> FERMENTING_BOTTLE_BLOCK_ENTITY = register("fermenting_bottle_block_entity", () -> ModBlockEntityTypes.FERMENTING_BOTTLE);
    public static final RegistryObject<BlockEntityType<AlembicBlockEntity>> ALEMBIC = register("alembic", AlembicBlockEntity::new, ModBlocks.ALEMBICS);
    public static final RegistryObject<BlockEntityType<CondenserBlockEntity>> CONDENSER = register("condenser", CondenserBlockEntity::new, ModBlocks.CONDENSER);


    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(factory, blocks.map(Supplier::get).toArray(Block[]::new)).build(null));
        //return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks);
    }

    private static <T extends BlockEntityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier)
    {
        return BLOCK_ENTITIES.register(name, supplier);
    }

    public static void register(IEventBus modBus)
    {
        BLOCK_ENTITIES.register(modBus);
    }
}
