package com.daderpduck.hallucinocraft.client.rendering.blockentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.state.BlockState;

import com.daderpduck.hallucinocraft.blocks.entities.CondenserBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.config.TFCConfig;

public class CondenserBlockEntityRenderer implements BlockEntityRenderer<CondenserBlockEntity>
{
    @Override
    public void render(CondenserBlockEntity condenser, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        BlockState state = condenser.getBlockState();

        condenser.getCapability(Capabilities.FLUID).map(handler -> handler.getFluidInTank(0)).filter(fluid -> !fluid.isEmpty()).ifPresent(fluidStack -> {
            RenderHelpers.renderFluidFace(poseStack, fluidStack, buffer, 0.1875F, 0.1875F, 0.8125F, 0.8125F, 0.140625F + (0.75F - 0.015625F) * fluidStack.getAmount() / TFCConfig.SERVER.barrelCapacity.get(), combinedOverlay, combinedLight);
        });
    }
}