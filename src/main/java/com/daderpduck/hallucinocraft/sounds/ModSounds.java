package com.daderpduck.hallucinocraft.sounds;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Hallucinocraft.MOD_ID);

    public static final RegistryObject<SoundEvent> BONG_HIT = register("item.bong.hit");
    public static final RegistryObject<SoundEvent> AMBIENT_SOUL_WRENCHER_LOOP = register("ambience.soul_wrencher.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_SOUL_WRENCHER_ADDITIONS = register("ambience.soul_wrencher.additions");

    public static final RegistryObject<SoundEvent> AMBIENT_SOUL_RESTER_LOOP = register("ambience.soul_rester.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_RED_SHROOM_LOOP = register("ambience.red_shroom.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_BROWN_SHROOM_LOOP = register("ambience.brown_shroom.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_PEYOTE_LOOP = register("ambience.peyote.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_CANNABIS_LOOP = register("ambience.cannabis.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_COCAINE_LOOP = register("ambience.cocaine.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_TOBACCO_LOOP = register("ambience.tobacco.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_MORPHINE_LOOP = register("ambience.morphine.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_OPIUM_LOOP = register("ambience.opium.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_CAFFEINE_LOOP = register("ambience.caffeine.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_GLITCH_LOOP = register("ambience.glitch.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_LSD_LOOP = register("ambience.lsd.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_DMT_LOOP = register("ambience.dmt.loop");
    public static final RegistryObject<SoundEvent> AMBIENT_NICOTINE_LOOP = register("ambience.nicotine.loop");

    public static final RegistryObject<SoundEvent> AMBIENT_SOUL_RESTER_ADDITIONS = register("ambience.soul_rester.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_RED_SHROOM_ADDITIONS = register("ambience.red_shroom.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_BROWN_SHROOM_ADDITIONS = register("ambience.brown_shroom.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_PEYOTE_ADDITIONS = register("ambience.peyote.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_CANNABIS_ADDITIONS = register("ambience.cannabis.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_COCAINE_ADDITIONS = register("ambience.cocaine.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_TOBACCO_ADDITIONS = register("ambience.tobacco.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_MORPHINE_ADDITIONS = register("ambience.morphine.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_OPIUM_ADDITIONS = register("ambience.opium.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_CAFFEINE_ADDITIONS = register("ambience.caffeine.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_GLITCH_ADDITIONS = register("ambience.glitch.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_LSD_ADDITIONS = register("ambience.lsd.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_DMT_ADDITIONS = register("ambience.dmt.additions");
    public static final RegistryObject<SoundEvent> AMBIENT_NICOTINE_ADDITIONS = register("ambience.nicotine.additions");


    private static RegistryObject<SoundEvent> register(String key) {
        return register(key, () -> new SoundEvent(new ResourceLocation(Hallucinocraft.MOD_ID, key)));
    }

    private static RegistryObject<SoundEvent> register(String name, Supplier<SoundEvent> supplier) {
        return SOUNDS.register(name, supplier);
    }

    public static void register(IEventBus modBus) {
        SOUNDS.register(modBus);
    }
}
