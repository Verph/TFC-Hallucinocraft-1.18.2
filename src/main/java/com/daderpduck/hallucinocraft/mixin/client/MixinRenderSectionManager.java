package com.daderpduck.hallucinocraft.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.MinecraftForge;

import org.spongepowered.asm.mixin.*;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkCameraContext;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.RegionChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.format.ChunkModelVertexFormats;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPassManager;

import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;

@Pseudo
@Mixin(RenderSectionManager.class)
public class MixinRenderSectionManager
{
    @Shadow private final ChunkRenderList chunkRenderList = new ChunkRenderList();
    @Shadow private final RegionChunkRenderer chunkRenderer;

    public MixinRenderSectionManager(SodiumWorldRenderer worldRenderer, BlockRenderPassManager renderPassManager, ClientLevel world, int renderDistance, CommandList commandList)
    {
        this.chunkRenderer = new RegionChunkRenderer(RenderDevice.INSTANCE, ChunkModelVertexFormats.DEFAULT);
    }

    @Overwrite(remap = false)
    public void renderLayer(ChunkRenderMatrices matrices, BlockRenderPass pass, double x, double y, double z)
    {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.LevelShaderEvent());
        RenderDevice device = RenderDevice.INSTANCE;
        CommandList commandList = device.createCommandList();

        MinecraftForge.EVENT_BUS.post(new RenderEvent.LevelShaderEvent());
        this.chunkRenderer.render(matrices, commandList, this.chunkRenderList, pass, new ChunkCameraContext(x, y, z));

        commandList.flush();
    }
}
