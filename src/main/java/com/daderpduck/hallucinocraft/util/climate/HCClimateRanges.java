package com.daderpduck.hallucinocraft.util.climate;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.RegisteredDataManager;
import net.dries007.tfc.util.climate.ClimateRange;

import com.daderpduck.hallucinocraft.blocks.crop.HCCrop;
import com.daderpduck.hallucinocraft.util.HCHelpers;

public class HCClimateRanges
{
    public static final Map<HCCrop, Supplier<ClimateRange>> CROPS = Helpers.mapOfKeys(HCCrop.class, crop -> register("crop/" + crop.getSerializedName()));

    private static RegisteredDataManager.Entry<ClimateRange> register(String name)
    {
        return ClimateRange.MANAGER.register(HCHelpers.identifier(name.toLowerCase(Locale.ROOT)));
    }
}
