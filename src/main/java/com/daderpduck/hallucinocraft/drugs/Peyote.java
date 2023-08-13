package com.daderpduck.hallucinocraft.drugs;

public class Peyote extends Drug {
    public Peyote(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.BIG_WAVES.addValue(effect*0.2F);
        drugEffects.SMALL_WAVES.addValue(effect*0.6F);
        drugEffects.WIGGLE_WAVES.addValue(effect*0.614F);
        drugEffects.WORLD_DEFORMATION.addValue(effect*2.5F);
        drugEffects.SATURATION.addValue(effect*3.345F);
        drugEffects.HUE_AMPLITUDE.addValue(effect*2.2F);
        drugEffects.CAMERA_TREMBLE.addValue(effect*0.1F);
        if (effect > 0.4) {
            drugEffects.KALEIDOSCOPE_INTENSITY.addValue((effect - 0.4F)*3.5F);
            drugEffects.RECURSION.addValue((effect - 0.4F)*6.5F);
        }
        if (effect > 0.25F)
        {
            drugEffects.PEYOTE_AMBIENCE.addValue(effect);
        }
    }
}
