package com.daderpduck.hallucinocraft.drugs;

public class Glitch extends Drug {
    public Glitch(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.GLITCH.addValue(effect);
        drugEffects.PITCH_RANDOM_SCALE.addValue(effect*2.5F);
        drugEffects.DISTORTION.addValue(effect*1.2F);
        drugEffects.SOUL_WRENCHER_AMBIENCE.addValue(effect);
        drugEffects.MUFFLE.addValue(effect*-6.9F);
        drugEffects.ECHO.addValue(effect*3.5F);
        drugEffects.TREBLE.addValue(effect*3F);
        drugEffects.REVERB.addValue(effect*4.2F);
        if (effect > 0.25F)
        {
            drugEffects.GLITCH_AMBIENCE.addValue(effect);
        }
    }
}
