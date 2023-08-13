package com.daderpduck.hallucinocraft.client.audio;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.events.hooks.SoundEvent;
import com.daderpduck.hallucinocraft.sounds.DrugLoopSoundInstance;
import com.daderpduck.hallucinocraft.sounds.ModSounds;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class SoundEventHandler
{
    @SubscribeEvent
    public static void onPlay(SoundEvent.Play event)
    {
        SoundProcessor.processSound(event.source, event.position.x, event.position.y, event.position.z);
    }

    @SubscribeEvent
    public static void onPitch(SoundEvent.SetPitch event)
    {
        Float pitch = SoundProcessor.modifyPitch(event.position.x, event.position.y, event.position.z, event.pitch);
        if (pitch != null)
        {
            event.setCanceled(true);
            event.pitch = pitch;
        }
    }

    private static final Random RANDOM = new Random();
    private static final List<Channel> channels = new LinkedList<>();

    private static DrugLoopSoundInstance soulWrencherLoop;
    private static DrugLoopSoundInstance soulResterLoop;
    private static DrugLoopSoundInstance redShroomLoop;
    private static DrugLoopSoundInstance brownShroomLoop;
    private static DrugLoopSoundInstance peyoteLoop;
    private static DrugLoopSoundInstance cannabisLoop;
    private static DrugLoopSoundInstance cocaineLoop;
    private static DrugLoopSoundInstance tobaccoLoop;
    private static DrugLoopSoundInstance morphineLoop;
    private static DrugLoopSoundInstance opiumLoop;
    private static DrugLoopSoundInstance caffeineLoop;
    private static DrugLoopSoundInstance glitchLoop;
    private static DrugLoopSoundInstance lsdLoop;
    private static DrugLoopSoundInstance dmtLoop;
    private static DrugLoopSoundInstance nicotineLoop;

    private static float soulWrencherLoopTicks = 0F;
    private static float soulResterLoopTicks = 0F;
    private static float redShroomLoopTicks = 0F;
    private static float brownShroomLoopTicks = 0F;
    private static float peyoteLoopTicks = 0F;
    private static float cannabisLoopTicks = 0F;
    private static float cocaineLoopTicks = 0F;
    private static float tobaccoLoopTicks = 0F;
    private static float morphineLoopTicks = 0F;
    private static float opiumLoopTicks = 0F;
    private static float caffeineLoopTicks = 0F;
    private static float glitchLoopTicks = 0F;
    private static float lsdLoopTicks = 0F;
    private static float dmtLoopTicks = 0F;
    private static float nicotineLoopTicks = 0F;

    private int delayUntilHeartbeat;
    private int delayUntilBreath;
    private boolean lastBreathWasIn;

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        SoundManager soundManager = mc.getSoundManager();
        float effectSoulWrencher = Drug.getDrugEffects().SOUL_WRENCHER_AMBIENCE.getClamped();
        float effectSoulRester = Drug.getDrugEffects().SOUL_RESTER_AMBIENCE.getClamped();
        float effectRedShroom = Drug.getDrugEffects().RED_SHROOM_AMBIENCE.getClamped();
        float effectBrownShroom = Drug.getDrugEffects().BROWN_SHROOM_AMBIENCE.getClamped();
        float effectPeyote = Drug.getDrugEffects().PEYOTE_AMBIENCE.getClamped();
        float effectCannabis = Drug.getDrugEffects().CANNABIS_AMBIENCE.getClamped();
        float effectCocaine = Drug.getDrugEffects().COCAINE_AMBIENCE.getClamped();
        float effectTobacco = Drug.getDrugEffects().TOBACCO_AMBIENCE.getClamped();
        float effectMorphine = Drug.getDrugEffects().MORPHINE_AMBIENCE.getClamped();
        float effectOpium = Drug.getDrugEffects().OPIUM_AMBIENCE.getClamped();
        float effectCaffeine = Drug.getDrugEffects().CAFFEINE_AMBIENCE.getClamped();
        float effectGlitch = Drug.getDrugEffects().GLITCH_AMBIENCE.getClamped();
        float effectLSD = Drug.getDrugEffects().LSD_AMBIENCE.getClamped();
        float effectDMT = Drug.getDrugEffects().DMT_AMBIENCE.getClamped();
        float effectNicotine = Drug.getDrugEffects().NICOTINE_AMBIENCE.getClamped();

        if (effectSoulWrencher > 0F)
        {
            if (soulWrencherLoop == null)
            {
                soulWrencherLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_SOUL_WRENCHER_LOOP.get());
                soundManager.play(soulWrencherLoop);
            }
            soulWrencherLoop.setVolume(effectSoulWrencher);

            soulWrencherLoopTicks += RANDOM.nextFloat()*effectSoulWrencher;
            if (soulWrencherLoopTicks > 100F)
            {
                soulWrencherLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_SOUL_WRENCHER_ADDITIONS.get()));
            }
        }
        else if (soulWrencherLoop != null)
        {
            soulWrencherLoop.setVolume(0F);
            soulWrencherLoopTicks = 0F;
            soulWrencherLoop = null;
        }

        if (effectSoulRester > 0F)
        {
            if (soulResterLoop == null)
            {
                soulResterLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_SOUL_RESTER_LOOP.get());
                soundManager.play(soulResterLoop);
            }
            soulResterLoop.setVolume(effectSoulRester);

            soulResterLoopTicks += RANDOM.nextFloat()*effectSoulRester;
            if (soulResterLoopTicks > 100F)
            {
                soulResterLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_SOUL_RESTER_ADDITIONS.get()));
            }
        }
        else if (soulResterLoop != null)
        {
            soulResterLoop.setVolume(0F);
            soulResterLoopTicks = 0F;
            soulResterLoop = null;
        }

        if (effectRedShroom > 0F)
        {
            if (redShroomLoop == null)
            {
                redShroomLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_RED_SHROOM_LOOP.get());
                soundManager.play(redShroomLoop);
            }
            redShroomLoop.setVolume(effectRedShroom);

            redShroomLoopTicks += RANDOM.nextFloat()*effectRedShroom;
            if (redShroomLoopTicks > 100F)
            {
                redShroomLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_RED_SHROOM_ADDITIONS.get()));
            }
        }
        else if (redShroomLoop != null)
        {
            redShroomLoop.setVolume(0F);
            redShroomLoopTicks = 0F;
            redShroomLoop = null;
        }

        if (effectBrownShroom > 0F)
        {
            if (brownShroomLoop == null)
            {
                brownShroomLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_BROWN_SHROOM_LOOP.get());
                soundManager.play(brownShroomLoop);
            }
            brownShroomLoop.setVolume(effectBrownShroom);

            brownShroomLoopTicks += RANDOM.nextFloat()*effectBrownShroom;
            if (brownShroomLoopTicks > 100F)
            {
                brownShroomLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_BROWN_SHROOM_ADDITIONS.get()));
            }
        }
        else if (brownShroomLoop != null)
        {
            brownShroomLoop.setVolume(0F);
            brownShroomLoopTicks = 0F;
            brownShroomLoop = null;
        }

        if (effectPeyote > 0F)
        {
            if (peyoteLoop == null)
            {
                peyoteLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_PEYOTE_LOOP.get());
                soundManager.play(peyoteLoop);
            }
            peyoteLoop.setVolume(effectPeyote);

            peyoteLoopTicks += RANDOM.nextFloat()*effectPeyote;
            if (peyoteLoopTicks > 100F)
            {
                peyoteLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_PEYOTE_ADDITIONS.get()));
            }
        }
        else if (peyoteLoop != null)
        {
            peyoteLoop.setVolume(0F);
            peyoteLoopTicks = 0F;
            peyoteLoop = null;
        }

        if (effectCannabis > 0F)
        {
            if (cannabisLoop == null)
            {
                cannabisLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_OPIUM_LOOP.get());
                soundManager.play(cannabisLoop);
            }
            cannabisLoop.setVolume(effectCannabis);

            cannabisLoopTicks += RANDOM.nextFloat()*effectCannabis;
            if (cannabisLoopTicks > 100F)
            {
                cannabisLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_OPIUM_ADDITIONS.get()));
            }
        }
        else if (cannabisLoop != null)
        {
            cannabisLoop.setVolume(0F);
            cannabisLoopTicks = 0F;
            cannabisLoop = null;
        }

        if (effectCocaine > 0F)
        {
            if (cocaineLoop == null)
            {
                cocaineLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_COCAINE_LOOP.get());
                soundManager.play(cocaineLoop);
            }
            cocaineLoop.setVolume(effectCocaine);

            cocaineLoopTicks += RANDOM.nextFloat()*effectCocaine;
            if (cocaineLoopTicks > 100F)
            {
                cocaineLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_COCAINE_ADDITIONS.get()));
            }
        }
        else if (cocaineLoop != null)
        {
            cocaineLoop.setVolume(0F);
            cocaineLoopTicks = 0F;
            cocaineLoop = null;
        }

        if (effectTobacco > 0F)
        {
            if (tobaccoLoop == null)
            {
                tobaccoLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_TOBACCO_LOOP.get());
                soundManager.play(tobaccoLoop);
            }
            tobaccoLoop.setVolume(effectTobacco);

            tobaccoLoopTicks += RANDOM.nextFloat()*effectTobacco;
            if (tobaccoLoopTicks > 100F)
            {
                tobaccoLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_TOBACCO_ADDITIONS.get()));
            }
        }
        else if (tobaccoLoop != null)
        {
            tobaccoLoop.setVolume(0F);
            tobaccoLoopTicks = 0F;
            tobaccoLoop = null;
        }

        if (effectMorphine > 0F)
        {
            if (morphineLoop == null)
            {
                morphineLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_MORPHINE_LOOP.get());
                soundManager.play(morphineLoop);
            }
            morphineLoop.setVolume(effectMorphine);

            morphineLoopTicks += RANDOM.nextFloat()*effectMorphine;
            if (morphineLoopTicks > 100F)
            {
                morphineLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_MORPHINE_ADDITIONS.get()));
            }
        }
        else if (morphineLoop != null)
        {
            morphineLoop.setVolume(0F);
            morphineLoopTicks = 0F;
            morphineLoop = null;
        }

        if (effectOpium > 0F)
        {
            if (opiumLoop == null)
            {
                opiumLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_OPIUM_LOOP.get());
                soundManager.play(opiumLoop);
            }
            opiumLoop.setVolume(effectOpium);

            opiumLoopTicks += RANDOM.nextFloat()*effectOpium;
            if (opiumLoopTicks > 100F)
            {
                opiumLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_OPIUM_ADDITIONS.get()));
            }
        }
        else if (opiumLoop != null)
        {
            opiumLoop.setVolume(0F);
            opiumLoopTicks = 0F;
            opiumLoop = null;
        }

        if (effectCaffeine > 0F)
        {
            if (caffeineLoop == null)
            {
                caffeineLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_CAFFEINE_LOOP.get());
                soundManager.play(caffeineLoop);
            }
            caffeineLoop.setVolume(effectCaffeine);

            caffeineLoopTicks += RANDOM.nextFloat()*effectCaffeine;
            if (caffeineLoopTicks > 100F)
            {
                caffeineLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_CAFFEINE_ADDITIONS.get()));
            }
        }
        else if (caffeineLoop != null)
        {
            caffeineLoop.setVolume(0F);
            caffeineLoopTicks = 0F;
            caffeineLoop = null;
        }

        if (effectGlitch > 0F)
        {
            if (glitchLoop == null)
            {
                glitchLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_GLITCH_LOOP.get());
                soundManager.play(glitchLoop);
            }
            glitchLoop.setVolume(effectGlitch);

            glitchLoopTicks += RANDOM.nextFloat()*effectGlitch;
            if (glitchLoopTicks > 100F)
            {
                glitchLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_GLITCH_ADDITIONS.get()));
            }
        }
        else if (glitchLoop != null)
        {
            glitchLoop.setVolume(0F);
            glitchLoopTicks = 0F;
            glitchLoop = null;
        }

        if (effectLSD > 0F)
        {
            if (lsdLoop == null)
            {
                lsdLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_LSD_LOOP.get());
                soundManager.play(lsdLoop);
            }
            lsdLoop.setVolume(effectLSD);

            lsdLoopTicks += RANDOM.nextFloat()*effectLSD;
            if (lsdLoopTicks > 100F)
            {
                lsdLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_LSD_ADDITIONS.get()));
            }
        }
        else if (lsdLoop != null)
        {
            lsdLoop.setVolume(0F);
            lsdLoopTicks = 0F;
            lsdLoop = null;
        }

        if (effectDMT > 0F)
        {
            if (dmtLoop == null)
            {
                dmtLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_DMT_LOOP.get());
                soundManager.play(dmtLoop);
            }
            dmtLoop.setVolume(effectDMT);

            dmtLoopTicks += RANDOM.nextFloat()*effectDMT;
            if (dmtLoopTicks > 100F)
            {
                dmtLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_DMT_ADDITIONS.get()));
            }
        }
        else if (dmtLoop != null)
        {
            dmtLoop.setVolume(0F);
            dmtLoopTicks = 0F;
            dmtLoop = null;
        }

        if (effectNicotine > 0F)
        {
            if (nicotineLoop == null)
            {
                nicotineLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_NICOTINE_LOOP.get());
                soundManager.play(nicotineLoop);
            }
            nicotineLoop.setVolume(effectNicotine);

            nicotineLoopTicks += RANDOM.nextFloat()*effectNicotine;
            if (nicotineLoopTicks > 100F)
            {
                nicotineLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_NICOTINE_ADDITIONS.get()));
            }
        }
        else if (nicotineLoop != null)
        {
            nicotineLoop.setVolume(0F);
            nicotineLoopTicks = 0F;
            nicotineLoop = null;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if (Minecraft.getInstance().player == null) return;
        if (event.phase == TickEvent.Phase.START)
        {
            channels.removeIf(Channel::stopped);
            Drug.getDrugEffects().ARTIFACTS.setValue(channels.isEmpty() ? 0F : (1F + Drug.getDrugEffects().GLITCH.getClamped()));
        }
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundSourceEvent event)
    {
        if (event.getSound().getLocation().equals(ModSounds.AMBIENT_SOUL_WRENCHER_ADDITIONS.get().getLocation()))
        {
            if (event.getChannel().playing())
                channels.add(event.getChannel());
        }
    }
}
