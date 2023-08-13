package com.daderpduck.hallucinocraft.items;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.dries007.tfc.util.Helpers;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.blocks.crop.HCCrop;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.Drugs;
import com.daderpduck.hallucinocraft.fluid.HCAlcohol;
import com.daderpduck.hallucinocraft.fluid.HCSimpleFluid;
import com.daderpduck.hallucinocraft.fluid.ModFluids;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hallucinocraft.MOD_ID);

    public static final Map<HCCrop, RegistryObject<Item>> CROP_SEEDS = Helpers.mapOfKeys(HCCrop.class, crop ->
        registerItem("seeds/" + crop.name(), () -> new ItemNameBlockItem(ModBlocks.CROPS.get(crop).get(), new Item.Properties().tab(Hallucinocraft.TAB)))
    );
    
    public static final RegistryObject<Item> HOP = registerItem("hop", () -> new Item(new Item.Properties().food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> INDIGO = registerItem("indigo");
    public static final RegistryObject<Item> MADDER = registerItem("madder");
    public static final RegistryObject<Item> WELD = registerItem("weld");
    public static final RegistryObject<Item> WOAD = registerItem("woad");

    public static final RegistryObject<Item> COTTON = registerItem("cotton");
    public static final RegistryObject<Item> COTTON_YARN = registerItem("cotton_yarn");
    public static final RegistryObject<Item> COTTON_CLOTH = registerItem("cotton_cloth");

    public static final RegistryObject<Item> FLAX = registerItem("flax");
    public static final RegistryObject<Item> FLAX_FIBER = registerItem("flax_fiber");
    public static final RegistryObject<Item> LINEN_FABRIC = registerItem("linen_fabric");
    public static final RegistryObject<Item> LINEN_NET = registerItem("linen_net");
    public static final RegistryObject<Item> DIRTY_LINEN_NET = registerItem("dirty_linen_net");

    public static final RegistryObject<Item> HEMP = registerItem("hemp");
    public static final RegistryObject<Item> HEMP_FIBER = registerItem("hemp_fiber");
    public static final RegistryObject<Item> HEMP_FABRIC = registerItem("hemp_fabric");
    public static final RegistryObject<Item> HEMP_NET = registerItem("hemp_net");
    public static final RegistryObject<Item> DIRTY_HEMP_NET = registerItem("dirty_hemp_net");

    public static final RegistryObject<Item> AGAVE = registerItem("agave", () -> new Item(new Item.Properties().food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> SISAL_FIBER = registerItem("sisal_fiber");
    public static final RegistryObject<Item> SISAL_FABRIC = registerItem("sisal_fabric");
    public static final RegistryObject<Item> SISAL_NET = registerItem("sisal_net");
    public static final RegistryObject<Item> DIRTY_SISAL_NET = registerItem("dirty_sisal_net");

    public static final RegistryObject<Item> PEYOTE = registerItem("peyote", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.PEYOTE, 500, 0.05F, 2500).food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> DRIED_PEYOTE = registerItem("dried_peyote", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.PEYOTE, 200, 0.5F, 7500).food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<JointItem> PEYOTE_JOINT = registerJoint("peyote_joint", new DrugChain().add(Drugs.PEYOTE, 0, 0.65F, 6500));

    public static final RegistryObject<Item> COFFEA = registerItem("coffea", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.CAFFEINE, 1000, 0.05F, 2000).food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> DRIED_COFFEA = registerItem("dried_coffea", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.CAFFEINE, 700, 0.1F, 2500).food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> ROASTED_COFFEE = registerItem("roasted_coffee", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.CAFFEINE, 500, 0.5F, 4000).food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));
    //public static final RegistryObject<Item> COFFEE_JUG = registerItem("ceramic/jug/coffee", () -> new JugItem(new DrugItem.Properties().addDrug(Drugs.CAFFEINE, 0, 0.6F, 5000).tab(Hallucinocraft.TAB).stacksTo(1), TFCConfig.SERVER.jugCapacity, ModTags.Fluids.COFFEE));

    public static final RegistryObject<Item> DRIED_RED_MUSHROOM = registerItem("dried_red_mushroom");
    public static final RegistryObject<DrugItem> RED_SHROOMS = registerDrug("red_shrooms", new DrugChain().add(Drugs.RED_SHROOMS, 400, 0.3F, 6900));

    public static final RegistryObject<Item> DRIED_BROWN_MUSHROOM = registerItem("dried_brown_mushroom");
    public static final RegistryObject<DrugItem> BROWN_SHROOMS = registerDrug("brown_shrooms", new DrugChain().add(Drugs.BROWN_SHROOMS, 200, 0.3F, 3200));

    public static final RegistryObject<DrugBowlItem> SHROOM_STEW = registerItem("shroom_stew", () -> new DrugBowlItem(new DrugItem.Properties().addDrug(Drugs.RED_SHROOMS, 400, 0.2F, 6900).addDrug(Drugs.BROWN_SHROOMS, 400, 0.2F, 6900).food(ModFoods.SHROOM_STEW).stacksTo(1).tab(Hallucinocraft.TAB)));

    public static final RegistryObject<Item> COCA_LEAF = registerItem("coca_leaf");
    public static final RegistryObject<Item> COCA_MULCH = registerItem("coca_mulch");
    public static final RegistryObject<DrugItem> COCAINE_DUST = registerDrug("cocaine_dust", new DrugChain().add(Drugs.COCAINE, 100, 0.05F, 1000), UseAnim.BOW, 64);
    public static final RegistryObject<DrugItem> COCAINE_POWDER = registerDrug("cocaine_powder", new DrugChain().add(Drugs.COCAINE, 100, 0.3F, 3200), UseAnim.BOW, 64);
    public static final RegistryObject<DrugItem> COCAINE_ROCK = registerDrug("cocaine_rock", new DrugChain().add(Drugs.COCAINE, 200, 0.1F, 4000), UseAnim.BOW, 64);
    public static final RegistryObject<Item> COKE_CAKE = registerBlock("coke_cake", ModBlocks.COKE_CAKE_BLOCK, 1);
    public static final RegistryObject<DrugBottleItem> UNBREWED_COCA_TEA = registerItem("unbrewed_coca_tea", () -> new DrugBottleItem(new DrugItem.Properties().addDrug(Drugs.COCAINE, 800, 0.01F, 1200).food(ModFoods.CANNABIS_TEA).stacksTo(16).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<DrugBottleItem> COCA_TEA = registerItem("coca_tea", () -> new DrugBottleItem(new DrugItem.Properties().addDrug(Drugs.COCAINE, 800, 0.1F, 3200).food(ModFoods.CANNABIS_TEA).stacksTo(16).tab(Hallucinocraft.TAB)));
    //public static final RegistryObject<Item> COCA_JUG = registerItem("ceramic/jug/coca", () -> new JugItem(new DrugItem.Properties().addDrug(Drugs.COCAINE, 0, 0.6F, 5000).tab(Hallucinocraft.TAB).stacksTo(1), TFCConfig.SERVER.jugCapacity, ModTags.Fluids.COCA_TEA));

    public static final RegistryObject<Item> CANNABIS_LEAF = registerItem("cannabis_leaf");
    public static final RegistryObject<Item> DRIED_CANNABIS_LEAF = registerItem("dried_cannabis_leaf");
    public static final RegistryObject<Item> CANNABIS_BUD = registerItem("cannabis_bud");
    public static final RegistryObject<Item> DRIED_CANNABIS_BUD = registerItem("dried_cannabis_bud");
    public static final RegistryObject<JointItem> CANNABIS_JOINT = registerJoint("cannabis_joint", new DrugChain().add(Drugs.CANNABIS, 0, 0.12F, 3200));
    public static final RegistryObject<Item> HASH_MUFFIN = registerItem("hash_muffin", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.CANNABIS, 800, 0.12F, 3200).food(ModFoods.HASH_MUFFIN).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<DrugBottleItem> UNBREWED_CANNABIS_TEA = registerItem("unbrewed_cannabis_tea", () -> new DrugBottleItem(new DrugItem.Properties().addDrug(Drugs.CANNABIS, 800, 0.01F, 1200).food(ModFoods.CANNABIS_TEA).stacksTo(16).stacksTo(16).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<DrugBottleItem> CANNABIS_TEA = registerItem("cannabis_tea", () -> new DrugBottleItem(new DrugItem.Properties().addDrug(Drugs.CANNABIS, 800, 0.1F, 3200).food(ModFoods.CANNABIS_TEA).stacksTo(16).tab(Hallucinocraft.TAB)));
    //public static final RegistryObject<Item> CANNABIS_JUG = registerItem("ceramic/jug/cannabis", () -> new JugItem(new DrugItem.Properties().addDrug(Drugs.CANNABIS, 0, 0.6F, 5000).tab(Hallucinocraft.TAB).stacksTo(1), TFCConfig.SERVER.jugCapacity, ModTags.Fluids.CANNABIS_TEA));
    public static final RegistryObject<JointItem> CANNABIS_CIGAR = registerCigar("cannabis_cigar", new DrugChain().add(Drugs.CANNABIS, 500, 0.1F, 3200));

    public static final RegistryObject<Item> TOBACCO_LEAF = registerItem("tobacco_leaf");
    public static final RegistryObject<Item> DRIED_TOBACCO_LEAF = registerItem("dried_tobacco_leaf");
    public static final RegistryObject<Item> TOBACCO_BUD = registerItem("tobacco_bud");
    public static final RegistryObject<Item> DRIED_TOBACCO_BUD = registerItem("dried_tobacco_bud");
    public static final RegistryObject<JointItem> CIGARETTE = registerJoint("cigarette", new DrugChain().add(Drugs.NICOTINE, 500, 0.05F, 2400));
    public static final RegistryObject<JointItem> TOBACCO_CIGAR = registerCigar("tobacco_cigar", new DrugChain().add(Drugs.NICOTINE, 500, 0.1F, 3200));

    public static final RegistryObject<Item> RED_PILL = registerItem("red_pill", () -> new DrugItem(new DrugItem.Properties().addDrug(Drugs.GLITCH, 500, 1F, 1337).food(ModFoods.GENERIC).tab(Hallucinocraft.TAB)));

    public static final RegistryObject<Item> EMPTY_SYRINGE = registerItem("syringe", 16);
    public static final RegistryObject<SyringeItem> COCAINE_SYRINGE = registerSyringe("cocaine_syringe", new DrugChain().add(Drugs.COCAINE, 0, 0.47F, 4800), 0xFFFFFFFF);
    public static final RegistryObject<SyringeItem> MORPHINE_SYRINGE = registerSyringe("morphine_syringe", new DrugChain().add(Drugs.MORPHINE, 0, 0.6F, 6200), 0xFF885038);
    public static final RegistryObject<SyringeItem> SOUL_RESTER_SYRINGE = registerSyringe("soul_rester_syringe", new DrugChain().add(Drugs.SOUL_RESTER, 0, 0.4F, 2400), 0xFF6A5244);
    public static final RegistryObject<SyringeItem> SOUL_WRENCHER_SYRINGE = registerSyringe("soul_wrencher_syringe", new DrugChain().add(Drugs.SOUL_WRENCHER, 0, 0.4F, 3600), 0xFF60F5FA);

    public static final RegistryObject<Item> BONG = registerItem("bong", () -> new BongItem(new Item.Properties().durability(8).setNoRepair().tab(Hallucinocraft.TAB)));

    public static final RegistryObject<Item> MORPHINE_BOTTLE = registerBottle("morphine_bottle");
    public static final RegistryObject<Item> OPIUM_BOTTLE_0 = registerItem("opium_bottle_0", 16);
    public static final RegistryObject<Item> OPIUM_BOTTLE_1 = registerItem("opium_bottle_1", 16);
    public static final RegistryObject<Item> OPIUM_BOTTLE_2 = registerItem("opium_bottle_2", 16);
    public static final RegistryObject<Item> OPIUM_BOTTLE_3 = registerItem("opium_bottle_3", 16);

    public static final RegistryObject<Item> SOUL_CONCENTRATE = registerItem("soul_concentrate", 64);
    public static final RegistryObject<Item> SOUL_RESTER_BOTTLE = registerBottle("soul_rester_bottle");
    public static final RegistryObject<Item> SOUL_WRENCHER_BOTTLE = registerBottle("soul_wrencher_bottle");

    public static final RegistryObject<Item> AIRLOCK = registerItem("airlock");
    public static final RegistryObject<Item> FERMENTING_BOTTLE = registerBlock("fermenting_bottle", ModBlocks.FERMENTING_BOTTLE_BLOCK);

    public static final Map<HCSimpleFluid, RegistryObject<BucketItem>> SIMPLE_FLUID_BUCKETS = Helpers.mapOfKeys(HCSimpleFluid.class, fluid ->
        registerItem("bucket/" + fluid.name(), () -> new BucketItem(ModFluids.SIMPLE_FLUIDS.get(fluid).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(Hallucinocraft.TAB)))
    );

    public static final Map<HCAlcohol, RegistryObject<BucketItem>> ALCOHOL_FLUID_BUCKETS = Helpers.mapOfKeys(HCAlcohol.class, fluid ->
        registerItem("bucket/" + fluid.name(), () -> new BucketItem(ModFluids.ALCOHOLS.get(fluid).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(Hallucinocraft.TAB)))
    );

    private static RegistryObject<DrugItem> registerDrug(String name, DrugChain drugChain)
    {
        return registerDrug(name, drugChain, UseAnim.EAT, 64);
    }

    private static RegistryObject<DrugItem> registerDrug(String name, DrugChain drugChain, UseAnim useAction, int stackSize)
    {
        DrugItem.Properties itemProperties = new DrugItem.Properties();
        for (DrugEffectProperty property : drugChain.list)
        {
            itemProperties.addDrug(property.drug, property.delayTicks, property.potencyPercentage, property.duration);
        }
        return registerItem(name, () -> new DrugItem(itemProperties.useAction(useAction).stacksTo(stackSize).tab(Hallucinocraft.TAB)));
    }

    private static RegistryObject<SyringeItem> registerSyringe(String name, DrugChain drugChain, int color)
    {
        SyringeItem.Properties itemProperties = new SyringeItem.Properties().color(color);
        for (DrugEffectProperty property : drugChain.list)
        {
            itemProperties.addDrug(property.drug, property.delayTicks, property.potencyPercentage, property.duration);
        }
        return registerItem(name, () -> new SyringeItem(itemProperties.tab(Hallucinocraft.TAB).stacksTo(1)));
    }

    private static RegistryObject<JointItem> registerJoint(String name, DrugChain drugChain)
    {
        DrugItem.Properties itemProperties = new DrugItem.Properties();
        for (DrugEffectProperty property : drugChain.list)
        {
            itemProperties.addDrug(property.drug, property.delayTicks, property.potencyPercentage, property.duration);
        }
        return registerItem(name, () -> new JointItem(itemProperties.useAction(UseAnim.BOW).stacksTo(16).tab(Hallucinocraft.TAB), 2));
    }

    private static RegistryObject<JointItem> registerCigar(String name, DrugChain drugChain)
    {
        DrugItem.Properties itemProperties = new DrugItem.Properties();
        for (DrugEffectProperty property : drugChain.list)
        {
            itemProperties.addDrug(property.drug, property.delayTicks, property.potencyPercentage, property.duration);
        }
        return registerItem(name, () -> new JointItem(itemProperties.useAction(UseAnim.BOW).stacksTo(16).tab(Hallucinocraft.TAB), 4));
    }

    private static <T extends Block> RegistryObject<Item> registerBlock(String name, RegistryObject<T> block)
    {
        return registerBlock(name, block, 64);
    }

    private static <T extends Block> RegistryObject<Item> registerBlock(String name, RegistryObject<T> block, int stackSize)
    {
        return registerItem(name, () -> new ItemNameBlockItem(block.get(), new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(stackSize)));
    }

    private static <T extends Block> RegistryObject<Item> registerBlockNamed(String name, RegistryObject<T> block)
    {
        return registerBlockNamed(name, block, 64);
    }

    private static <T extends Block> RegistryObject<Item> registerBlockNamed(String name, RegistryObject<T> block, int stackSize)
    {
        return registerItem(name, () -> new ItemNameBlockItem(block.get(), new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(stackSize)));
    }

    private static RegistryObject<Item> registerBottle(String name)
    {
        return registerItem(name, () -> new Item(new Item.Properties().stacksTo(16).tab(Hallucinocraft.TAB))
        {
            @Override
            public boolean hasContainerItem(ItemStack itemStack)
            {
                return true;
            }

            @Override
            public ItemStack getContainerItem(ItemStack itemStack)
            {
                return Items.GLASS_BOTTLE.getDefaultInstance();
            }
        });
    }

    private static RegistryObject<Item> registerItem(String name)
    {
        return registerItem(name, () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB)));
    }

    private static RegistryObject<Item> registerItem(String name, int stackSize)
    {
        return registerItem(name, () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(stackSize)));
    }

    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), supplier);
    }

    public static class DrugChain
    {
        public final List<DrugEffectProperty> list = new ArrayList<>();

        public DrugChain add(RegistryObject<Drug> drug, int delayTicks, float potencyPercentage, int duration)
        {
            list.add(new DrugEffectProperty(drug, delayTicks, potencyPercentage, duration));
            return this;
        }
    }

    public record DrugEffectProperty(RegistryObject<Drug> drug, int delayTicks, float potencyPercentage, int duration)
    {
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register(new SyringeItem.Color(), COCAINE_SYRINGE.get(), MORPHINE_SYRINGE.get(), SOUL_RESTER_SYRINGE.get(), SOUL_WRENCHER_SYRINGE.get());
    }

    public static void register(IEventBus modBus)
    {
        ITEMS.register(modBus);
        modBus.register(ModItems.class);
    }
}
