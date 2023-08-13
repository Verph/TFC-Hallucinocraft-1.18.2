package com.daderpduck.hallucinocraft.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.blocks.crop.HCCrop;
import com.daderpduck.hallucinocraft.blocks.entities.ModBlockEntities;
import com.daderpduck.hallucinocraft.client.rendering.blockentity.AlembicBlockEntityRenderer;
import com.daderpduck.hallucinocraft.client.rendering.blockentity.CondenserBlockEntityRenderer;
import com.daderpduck.hallucinocraft.client.screen.AlembicScreen;
import com.daderpduck.hallucinocraft.container.ModContainerTypes;

import net.dries007.tfc.client.TFCColors;
import net.dries007.tfc.util.Helpers;

public final class ClientEventHandler
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::clientSetup);
        bus.addListener(ClientEventHandler::onConfigReload);
        bus.addListener(ClientEventHandler::registerModelLoaders);
        bus.addListener(ClientEventHandler::registerColorHandlerBlocks);
        bus.addListener(ClientEventHandler::registerColorHandlerItems);
        bus.addListener(ClientEventHandler::registerParticleFactories);
        bus.addListener(ClientEventHandler::registerClientReloadListeners);
        bus.addListener(ClientEventHandler::registerEntityRenderers);
        bus.addListener(ClientEventHandler::registerLayerDefinitions);
        bus.addListener(ClientEventHandler::onTextureStitch);
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        // Screens
        event.enqueueWork(() -> {
            MenuScreens.register(ModContainerTypes.ALEMBIC.get(), AlembicScreen::new);

            ItemProperties.register(ModBlocks.ALEMBICS.get().asItem(), Helpers.identifier("connected"), (stack, level, entity, unused) -> stack.hasTag() ? 1.0f : 0f);
            ItemProperties.register(ModBlocks.CONDENSER.get().asItem(), Helpers.identifier("facing"), (stack, level, entity, unused) -> stack.hasTag() ? 1.0f : 0f);
        });

        // Render Types
        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();

        // Plants
        ModBlocks.CROPS.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));
        ModBlocks.DEAD_CROPS.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));
        ModBlocks.WILD_CROPS.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FERMENTING_BOTTLE_BLOCK.get(), cutout);

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ALEMBICS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CONDENSER.get(), cutout);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(ModBlockEntities.ALEMBIC.get(), ctx -> new AlembicBlockEntityRenderer());
        event.registerBlockEntityRenderer(ModBlockEntities.CONDENSER.get(), ctx -> new CondenserBlockEntityRenderer());
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    }

    public static void onConfigReload(ModConfigEvent.Reloading event)
    {
    }

    public static void registerModelLoaders(ModelRegistryEvent event)
    {
    }

    public static void registerColorHandlerBlocks(ColorHandlerEvent.Block event)
    {
        final BlockColors registry = event.getBlockColors();
        final BlockColor grassColor = (state, worldIn, pos, tintIndex) -> TFCColors.getGrassColor(pos, tintIndex);
        final BlockColor tallGrassColor = (state, worldIn, pos, tintIndex) -> TFCColors.getTallGrassColor(pos, tintIndex);
        final BlockColor foliageColor = (state, worldIn, pos, tintIndex) -> TFCColors.getFoliageColor(pos, tintIndex);
        final BlockColor seasonalFoliageColor = (state, worldIn, pos, tintIndex) -> TFCColors.getSeasonalFoliageColor(pos, tintIndex);

        ModBlocks.CROPS.forEach((crop, reg) -> {
            if (crop != HCCrop.PEYOTE)
                registry.register(grassColor, reg.get());
        });
        ModBlocks.DEAD_CROPS.forEach((crop, reg) -> {
            if (crop != HCCrop.PEYOTE)
                registry.register(grassColor, reg.get());
        });
        ModBlocks.WILD_CROPS.forEach((crop, reg) -> {
            if (crop != HCCrop.PEYOTE)
                registry.register(grassColor, reg.get());
        });
    }

    public static void registerColorHandlerItems(ColorHandlerEvent.Item event)
    {
        final ItemColors registry = event.getItemColors();
        final ItemColor grassColor = (stack, tintIndex) -> TFCColors.getGrassColor(null, tintIndex);
        final ItemColor seasonalFoliageColor = (stack, tintIndex) -> TFCColors.getFoliageColor(null, tintIndex);

        ModBlocks.WILD_CROPS.forEach((crop, reg) -> {
            if (crop != HCCrop.PEYOTE)
                registry.register(grassColor, reg.get().asItem());
        });
    }

    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
    {
    }

    public static void registerParticleFactories(ParticleFactoryRegisterEvent event)
    {
    }

    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
    }
}
