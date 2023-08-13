package com.daderpduck.hallucinocraft.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPassManager;

import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;

@Pseudo
@Mixin(SodiumWorldRenderer.class)
public class MixinRubidiumWorldRenderer
{
    /*@Shadow private RenderSectionManager renderSectionManager;
    @Shadow private BlockRenderPassManager renderPassManager;
    @Shadow private ChunkTracker chunkTracker;*/

    @SuppressWarnings("deprecation")
    @Inject(method = "drawChunkLayer", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/ShaderInstance;apply()V"))
    public void drawWobbly(CallbackInfo ci)
    {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.LevelShaderEvent());
    }
    
    /*@SuppressWarnings("deprecation")
    @Overwrite(remap = false)
    public void drawChunkLayer(RenderType pRenderType, PoseStack pPoseStack, double x, double y, double z)
    {
        BlockRenderPass pass = this.renderPassManager.getRenderPassForLayer(pRenderType);
        pass.startDrawing();

        MinecraftForge.EVENT_BUS.post(new RenderEvent.LevelShaderEvent());
        this.renderSectionManager.renderLayer(ChunkRenderMatrices.from(pPoseStack), pass, x, y, z);

        pass.endDrawing();
    }*/
}
