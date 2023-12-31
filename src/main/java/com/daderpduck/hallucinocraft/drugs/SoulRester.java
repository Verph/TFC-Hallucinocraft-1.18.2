package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.entity.player.Player;

public class SoulRester extends Drug {
    public SoulRester(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.SATURATION.addValue(effect*-0.75F);
        drugEffects.BLUR.addValue(effect*16F);
        drugEffects.FOG_DENSITY.addValue(effect);
        drugEffects.FOG_DARKEN.addValue(effect*4F);
        drugEffects.MUFFLE.addValue(effect*0.9F);
        drugEffects.ECHO.addValue(effect);
        drugEffects.SOUL_WRENCHER_AMBIENCE.addValue(effect);
        if (effect > 0.7F)
        {
            drugEffects.MOUSE_SENSITIVITY_SCALE.addValue((effect - 0.7F)*-2F);
        }
        if (effect > 0.25F)
        {
            drugEffects.SOUL_RESTER_AMBIENCE.addValue(effect);
        }
    }

    @Override
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*-0.9F);
        drugEffects.DIG_SPEED.addValue(effect*-0.9F);
    }
}
