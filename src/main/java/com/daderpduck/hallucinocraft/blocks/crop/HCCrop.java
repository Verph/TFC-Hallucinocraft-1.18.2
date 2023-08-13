package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity.NutrientType;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.climate.ClimateRange;

import com.daderpduck.hallucinocraft.blocks.entities.HCCropBlockEntity;
import com.daderpduck.hallucinocraft.blocks.entities.ModBlockEntities;
import com.daderpduck.hallucinocraft.items.ModItems;
import com.daderpduck.hallucinocraft.util.climate.HCClimateRanges;

public enum HCCrop implements StringRepresentable
{
    AGAVE(NutrientType.NITROGEN, 6),
    COCA(NutrientType.PHOSPHOROUS, 3, 3, false, ModItems.COCA_LEAF),
    COFFEA(NutrientType.PHOSPHOROUS, 4, 4, false, ModItems.COFFEA),
    COTTON(NutrientType.NITROGEN, 6, ModItems.COTTON),
    FLAX(NutrientType.POTASSIUM, 7),
    HEMP(NutrientType.POTASSIUM, 2, 3, false, ModItems.CANNABIS_BUD),
    HOP(NutrientType.NITROGEN, 2, 4, false, ModItems.HOP),
    INDIGO(NutrientType.PHOSPHOROUS, 6),
    MADDER(NutrientType.NITROGEN, 5),
    PEYOTE(NutrientType.POTASSIUM, 4),
    TOBACCO(NutrientType.NITROGEN, 3, 4, false, ModItems.TOBACCO_BUD),
    WELD(NutrientType.POTASSIUM, 5),
    WOAD(NutrientType.PHOSPHOROUS, 5);

    public static ExtendedProperties doubleCrop()
    {
        return dead().blockEntity(ModBlockEntities.CROP).serverTicks(HCCropBlockEntity::serverTickBottomPartOnly);
    }

    public static ExtendedProperties crop()
    {
        return dead().blockEntity(ModBlockEntities.CROP).serverTicks(HCCropBlockEntity::serverTick);
    }

    public static ExtendedProperties dead()
    {
        return ExtendedProperties.of(Material.PLANT).noCollission().randomTicks().strength(0.4F).sound(SoundType.CROP).flammable(60, 30);
    }

    public final String serializedName;
    public final NutrientType primaryNutrient;
    public final Supplier<Block> factory;
    public final Supplier<Block> deadFactory;
    public final Supplier<Block> wildFactory;
    public Supplier<? extends Item> productItem;

    HCCrop(NutrientType primaryNutrient, Function<HCCrop, Block> factory, Function<HCCrop, Block> deadFactory, Function<HCCrop, Block> wildFactory, Supplier<? extends Item> productItem)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.primaryNutrient = primaryNutrient;
        this.factory = () -> factory.apply(this);
        this.deadFactory = () -> deadFactory.apply(this);
        this.wildFactory = () -> wildFactory.apply(this);
        this.productItem = productItem;
    }

    HCCrop(NutrientType primaryNutrient, Function<HCCrop, Block> factory, Function<HCCrop, Block> deadFactory, Function<HCCrop, Block> wildFactory)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.primaryNutrient = primaryNutrient;
        this.factory = () -> factory.apply(this);
        this.deadFactory = () -> deadFactory.apply(this);
        this.wildFactory = () -> wildFactory.apply(this);
    }

    HCCrop(NutrientType primaryNutrient, int singleBlockStages, Supplier<? extends Item> productItem)
    {
        this(primaryNutrient, self -> HCDefaultCropBlock.create(crop(), singleBlockStages, self, self.getProductItem()), self -> new HCDeadCropBlock(dead(), self.getClimateRange()), self -> new HCWildCropBlock(dead(), self), productItem);
    }

    HCCrop(NutrientType primaryNutrient, int floodedSingleBlockStages, boolean flooded)
    {
        this(primaryNutrient, self -> HCFloodedCropBlock.create(crop(), floodedSingleBlockStages, self, self.getProductItem()), self -> new HCFloodedDeadCropBlock(dead(), self.getClimateRange()), self -> new HCFloodedWildCropBlock(dead()));
        assert flooded;
    }

    HCCrop(NutrientType primaryNutrient, int doubleBlockBottomStages, int doubleBlockTopStages, boolean requiresStick)
    {
        this(primaryNutrient, requiresStick ?
                self -> HCClimbingCropBlock.create(doubleCrop(), doubleBlockBottomStages, doubleBlockTopStages, self, self.getProductItem()) :
                self -> HCDoubleCropBlock.create(doubleCrop(), doubleBlockBottomStages, doubleBlockTopStages, self, self.getProductItem()),
            self -> new HCDeadClimbingCropBlock(dead(), self.getClimateRange()), self -> new HCWildDoubleCropBlock(dead(), self));
    }

    HCCrop(NutrientType primaryNutrient, int singleBlockStages)
    {
        this(primaryNutrient, self -> HCDefaultCropBlock.create(crop(), singleBlockStages, self, self.getProductItem()), self -> new HCDeadCropBlock(dead(), self.getClimateRange()), self -> new HCWildCropBlock(dead(), self));
    }

    HCCrop(NutrientType primaryNutrient, int spreadingSingleBlockStages, Supplier<Supplier<? extends Block>> fruit, Supplier<? extends Item> productItem)
    {
        this(primaryNutrient, self -> HCSpreadingCropBlock.create(crop(), spreadingSingleBlockStages, self, fruit, self.getProductItem()), self -> new HCDeadCropBlock(dead(), self.getClimateRange()), self -> new HCWildSpreadingCropBlock(dead(), fruit, self), productItem);
    }

    HCCrop(NutrientType primaryNutrient, int floodedSingleBlockStages, boolean flooded, Supplier<? extends Item> productItem)
    {
        this(primaryNutrient, self -> HCFloodedCropBlock.create(crop(), floodedSingleBlockStages, self, self.getProductItem()), self -> new HCFloodedDeadCropBlock(dead(), self.getClimateRange()), self -> new HCFloodedWildCropBlock(dead()), productItem);
        assert flooded;
    }

    HCCrop(NutrientType primaryNutrient, int doubleBlockBottomStages, int doubleBlockTopStages, boolean requiresStick, Supplier<? extends Item> productItem)
    {
        this(primaryNutrient, requiresStick ?
                self -> HCClimbingCropBlock.create(doubleCrop(), doubleBlockBottomStages, doubleBlockTopStages, self, self.getProductItem()) :
                self -> HCDoubleCropBlock.create(doubleCrop(), doubleBlockBottomStages, doubleBlockTopStages, self, self.getProductItem()),
            self -> new HCDeadClimbingCropBlock(dead(), self.getClimateRange()), self -> new HCWildDoubleCropBlock(dead(), self), 
            productItem);
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public Block create()
    {
        return factory.get();
    }

    public Block createDead()
    {
        return deadFactory.get();
    }

    public Block createWild()
    {
        return wildFactory.get();
    }

    public NutrientType getPrimaryNutrient()
    {
        return primaryNutrient;
    }

    public Supplier<ClimateRange> getClimateRange()
    {
        return HCClimateRanges.CROPS.get(this);
    }

    @Nullable
    public Supplier<? extends Item> getProductItem()
    {
        return productItem;
    }
}