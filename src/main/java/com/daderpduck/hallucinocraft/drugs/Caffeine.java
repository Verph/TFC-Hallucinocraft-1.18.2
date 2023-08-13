package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.entity.player.Player;

public class Caffeine extends Drug {
    public Caffeine(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.SATURATION.addValue(effect*-0.5F);
        drugEffects.CAMERA_TREMBLE.addValue(effect*5F);
        drugEffects.BUMPY.addValue(effect*400.0F);
        drugEffects.MOUSE_SENSITIVITY_SCALE.addValue(effect*1.75F);
        drugEffects.TREBLE.addValue(effect);
        if (effect > 0.25F)
        {
            drugEffects.CAFFEINE_AMBIENCE.addValue(effect);
        }
    }

    @Override
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*2F);
        drugEffects.DIG_SPEED.addValue(effect*2F);
        drugEffects.HUNGER_RATE.addValue(effect*8F);
        if (effect == 1.0) drugEffects.DROWN_RATE.addValue(2F);
        if (effect > 0.5F) drugEffects.REGENERATION_RATE.addValue((effect - 0.5F)*0.1F);
    }

    @Override
    public void abuseTick(Player player, DrugEffects drugEffects, int abuse) {
        if (abuse > 8000) {
            drugEffects.MOVEMENT_SPEED.addValue(-0.001F*(float)(abuse - 8000));
        }
        if (abuse > 12000) {
            drugEffects.DROWN_RATE.addValue(1F);
        }
    }
}
