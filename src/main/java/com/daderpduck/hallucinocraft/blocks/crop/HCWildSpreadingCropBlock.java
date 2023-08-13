package com.daderpduck.hallucinocraft.blocks.crop;

import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

import net.dries007.tfc.common.blocks.ExtendedProperties;

public class HCWildSpreadingCropBlock extends HCWildCropBlock
{
    private final Supplier<Supplier<? extends Block>> fruit;

    public HCWildSpreadingCropBlock(ExtendedProperties properties, Supplier<Supplier<? extends Block>> fruit, HCCrop crop)
    {
        super(properties, crop);
        this.fruit = fruit;
    }

    public Block getFruit()
    {
        return fruit.get().get();
    }
}
