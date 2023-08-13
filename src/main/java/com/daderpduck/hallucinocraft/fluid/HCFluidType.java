package com.daderpduck.hallucinocraft.fluid;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.level.material.Fluid;

// Merged enum
public record HCFluidType(String name, OptionalInt color, Supplier<? extends Fluid> fluid)
{
    private static final Map<Enum<?>, HCFluidType> IDENTITY = new HashMap<>();
    private static final List<HCFluidType> VALUES = Stream.of(
            Arrays.stream(HCSimpleFluid.values()).map(fluid -> fromEnum(fluid, fluid.getColor(), fluid.getId(), ModFluids.SIMPLE_FLUIDS.get(fluid).source())),
            Arrays.stream(HCAlcohol.values()).map(fluid -> fromEnum(fluid, fluid.getColor(), fluid.getId(), ModFluids.ALCOHOLS.get(fluid).source()))
        )
        .flatMap(Function.identity())
        .toList();

    public static <R> Map<HCFluidType, R> mapOf(Function<? super HCFluidType, ? extends R> map)
    {
        return VALUES.stream().collect(Collectors.toMap(Function.identity(), map));
    }

    public static HCFluidType asType(Enum<?> identity)
    {
        return IDENTITY.get(identity);
    }

    private static HCFluidType fromEnum(Enum<?> identity, int color, String name, Supplier<? extends Fluid> fluid)
    {
        final HCFluidType type = new HCFluidType(name, OptionalInt.of(ModFluids.ALPHA_MASK | color), fluid);
        IDENTITY.put(identity, type);
        return type;
    }
}
