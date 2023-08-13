package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import com.daderpduck.hallucinocraft.events.hooks.SetCameraEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks onto after camera matrix is set, upon chunk layer rendering, upon entity rendering, and upon tile entity rendering
 */
@Mixin(value = LevelRenderer.class, priority = 1)
public class MixinLevelRenderer
{
    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    private void onStart(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci)
    {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.LevelStartRender());
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci)
    {
        MinecraftForge.EVENT_BUS.post(new SetCameraEvent(pPoseStack, pPartialTick, pFinishNanoTime, pRenderBlockOutline, pCamera, pGameRenderer, pLightTexture, pProjectionMatrix));
    }

    // Stupid Rubidium breaking wobbly terrain!
    @Inject(method = "renderChunkLayer", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/ShaderInstance;apply()V"))
    private void onRenderChunkLayer(RenderType pRenderType, PoseStack pPoseStack, double pCamX, double pCamY, double pCamZ, Matrix4f pProjectionMatrix, CallbackInfo ci)
    {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.LevelShaderEvent());
    }
}
