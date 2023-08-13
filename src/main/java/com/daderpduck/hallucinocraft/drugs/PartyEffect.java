package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class PartyEffect extends Drug
{
    public PartyEffect(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void startUse(Player player) {
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.CAMERA_TREMBLE.addValue(effect*0.5F);
        drugEffects.WIGGLE_WAVES.addValue(effect*0.4F);
        drugEffects.BRIGHTNESS.addValue(effect*0.2F);
        drugEffects.SATURATION.addValue(effect*5.0F);
        drugEffects.HUE_AMPLITUDE.addValue(effect*2.8F);
        drugEffects.WORLD_DEFORMATION.addValue(effect*0.8F);
        if (effect > 0.5F) {
            float f = effect - 0.5F;
            drugEffects.WATER_DISTORT.addValue(Math.min(f*f, 0.08F));
            drugEffects.SATURATION.addValue(f);
        }
        if (effect > 0.25F)
        {
            drugEffects.LSD_AMBIENCE.addValue(effect);
        }
    }

    @Override
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 35, 0, true, true));
        if (effect == 4F) drugEffects.DROWN_RATE.addValue(3F);
    }
}

