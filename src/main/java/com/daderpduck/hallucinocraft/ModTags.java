package com.daderpduck.hallucinocraft;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import net.dries007.tfc.util.Helpers;

public class ModTags
{
    public static class Fluids
    {
        public static final TagKey<Fluid> COFFEE = create("coffee");
        public static final TagKey<Fluid> COCA_TEA = create("coca_tea");
        public static final TagKey<Fluid> CANNABIS_TEA = create("cannabis_tea");

        private static TagKey<Fluid> create(String id)
        {
            return TagKey.create(Registry.FLUID_REGISTRY, Helpers.identifier(id));
        }
    }
}