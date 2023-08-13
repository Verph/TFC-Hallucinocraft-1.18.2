package com.daderpduck.hallucinocraft;

import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.blocks.entities.ModBlockEntities;
import com.daderpduck.hallucinocraft.client.ClientEventHandler;
import com.daderpduck.hallucinocraft.client.ClientForgeEventHandler;
import com.daderpduck.hallucinocraft.client.rendering.shaders.LevelShaders;
import com.daderpduck.hallucinocraft.client.rendering.shaders.post.PostShaders;
import com.daderpduck.hallucinocraft.commands.SetDrugCommand;
import com.daderpduck.hallucinocraft.container.ModContainerTypes;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.Drugs;
import com.daderpduck.hallucinocraft.fluid.ModFluids;
import com.daderpduck.hallucinocraft.items.*;
import com.daderpduck.hallucinocraft.network.PacketHandler;
import com.daderpduck.hallucinocraft.recipe.ModRecipeSerializers;
import com.daderpduck.hallucinocraft.recipe.ModRecipes;
import com.daderpduck.hallucinocraft.sounds.ModSounds;
import com.daderpduck.hallucinocraft.world.feature.HCFeatures;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.function.Supplier;

@Mod(Hallucinocraft.MOD_ID)
public class Hallucinocraft
{
    public static final String MOD_ID = "hallucinocraft";
    public static final String MOD_VERSION = "${version}";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab TAB = new CreativeModeTab("creativetab")
    {
        @Nonnull
        @Override
        public ItemStack makeIcon()
        {
            return ModItems.RED_SHROOMS.get().getDefaultInstance();
        }
    };

    private static Supplier<IForgeRegistry<Drug>> drugSupplier;

    public Hallucinocraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, HallucinocraftConfig.serverSpec);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HallucinocraftConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HallucinocraftConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HallucinocraftConfig.commonSpec);
        modEventBus.register(HallucinocraftConfig.class);

        drugSupplier = Drugs.DRUGS.makeRegistry(Drug.class, Drugs::getRegistryBuilder);

        ModItems.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModContainerTypes.CONTAINERS.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModSounds.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        //ModBlockEntities.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        Drugs.register(modEventBus);
        ForgeEventHandler.init();
        HCFeatures.FEATURES.register(modEventBus);

        PacketHandler.init();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init();
            ClientForgeEventHandler.init();
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        Drugs.DrugRegistry = drugSupplier.get(); //TODO: Is this correct??
        CompostRegistry.register();
        CauldronRegistry.register();
        BrewRegistry.register();
        BongRegistry.register();
        SetDrugCommand.registerSerializer();
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        if (HallucinocraftConfig.CLIENT.useShaders.get())
        {
            if (HallucinocraftConfig.CLIENT.useLevelShaders.get())
                LevelShaders.setup();

            if (HallucinocraftConfig.CLIENT.usePostShaders.get())
            {
                try
                {
                    PostShaders.setup();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        /*InterModComms.sendTo("hallucinocraft", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });*/
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        /*LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));*/
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        SetDrugCommand.register(event.getDispatcher());
    }
}
