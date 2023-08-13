package com.daderpduck.hallucinocraft.drugs;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;

public class Drugs
{
    public static HashMap<String, RegistryObject<Drug>> DrugMap = new HashMap<String, RegistryObject<Drug>>();
    //public static Map<String, Drug> DrugMap = new HashMap<>();

    public static final DeferredRegister<Drug> DRUGS = DeferredRegister.create(new ResourceLocation(Hallucinocraft.MOD_ID, "drug"), Hallucinocraft.MOD_ID);
    public static IForgeRegistry<Drug> DrugRegistry;

    public static final RegistryObject<Drug> RED_SHROOMS = register("red_shrooms", () -> new RedShrooms(new Drug.DrugProperties().adsr(2400F, 200F, 0.8F, 2400F)));
    public static final RegistryObject<Drug> BROWN_SHROOMS = register("brown_shrooms", () -> new BrownShrooms(new Drug.DrugProperties().adsr(2400F, 0F, 1F, 2400F)));
    public static final RegistryObject<Drug> COCAINE = register("cocaine", () -> new Cocaine(new Drug.DrugProperties().adsr(800F, 0F, 1F, 1200F).abuse(2)));
    public static final RegistryObject<Drug> CANNABIS = register("cannabis", () -> new Cannabis(new Drug.DrugProperties().adsr(1800F, 0F, 1F, 2400F)));
    public static final RegistryObject<Drug> MORPHINE = register("morphine", () -> new Morphine(new Drug.DrugProperties().adsr(0F, 800F, 0.8F, 200F)));
    public static final RegistryObject<Drug> SOUL_RESTER = register("soul_rester", () -> new SoulRester(new Drug.DrugProperties().adsr(800F, 0F, 1F, 4800F)));
    public static final RegistryObject<Drug> SOUL_WRENCHER = register("soul_wrencher", () -> new SoulWrencher(new Drug.DrugProperties().adsr(2400F, 0F, 1F, 4800F)));
    public static final RegistryObject<Drug> CAFFEINE = register("caffeine", () -> new Caffeine(new Drug.DrugProperties().adsr(1200F, 0F, 1F, 4800F)));
    public static final RegistryObject<Drug> PEYOTE = register("peyote", () -> new Caffeine(new Drug.DrugProperties().adsr(2400F, 200F, 0.8F, 2400F)));
    public static final RegistryObject<Drug> GLITCH = register("glitch", () -> new Glitch(new Drug.DrugProperties().adsr(1200F, 0F, 1F, 4800F)));
    public static final RegistryObject<Drug> NICOTINE = register("nicotine", () -> new Nicotine(new Drug.DrugProperties().adsr(150F, 10F, 0.2F, 1000F)));
    public static final RegistryObject<Drug> PARTY = register("ecstasy", () -> new PartyEffect(new Drug.DrugProperties().adsr(500F, 200F, 0.8F, 2500F)));
    public static final RegistryObject<Drug> LSD_BOTTLE = register("lsd_bottle", () -> new LSD(new Drug.DrugProperties().adsr(2400F, 200F, 0.95F, 3000F)));
    public static final RegistryObject<Drug> LSD_BLOTTER = register("lsd_blotter", () -> new LSD(new Drug.DrugProperties().adsr(2400F, 200F, 0.8F, 2400F)));
    public static final RegistryObject<Drug> ORANGESUNSHINE_BOTTLE = register("orangesunshine_bottle", () -> new LSD(new Drug.DrugProperties().adsr(3100F, 300F, 0.66F, 3000F)));
    public static final RegistryObject<Drug> ORANGESUNSHINE_BLOTTER = register("orangesunshine_blotter", () -> new LSD(new Drug.DrugProperties().adsr(3100F, 300F, 0.66F, 2400F)));
    public static final RegistryObject<Drug> DMT = register("dmt", () -> new DMT(new Drug.DrugProperties().adsr(2400F, 200F, 4.0F, 3000F)));
    public static final RegistryObject<Drug> DMT_5_MEO = register("dmt_5_meo", () -> new DMT(new Drug.DrugProperties().adsr(2400F, 200F, 6.0F, 3000F)));

    private static RegistryObject<Drug> register(String name, Supplier<? extends Drug> supplier)
    {
        RegistryObject<Drug> drug = DRUGS.register(name, supplier);
        DrugMap.put(drug.getId().toString(), drug);
        return drug;
    }

    public static RegistryBuilder<Drug> getRegistryBuilder()
    {
        return new RegistryBuilder<Drug>()
                .setName(new ResourceLocation(Hallucinocraft.MOD_ID, "drug"))
                .setType(Drug.class);
    }

    public static void register(IEventBus modBus)
    {
        DRUGS.register(modBus);
    }
}
