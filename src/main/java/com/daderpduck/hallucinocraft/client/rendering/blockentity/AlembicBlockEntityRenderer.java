package com.daderpduck.hallucinocraft.client.rendering.blockentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.state.BlockState;

import com.daderpduck.hallucinocraft.blocks.entities.AlembicBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.config.TFCConfig;

public class AlembicBlockEntityRenderer implements BlockEntityRenderer<AlembicBlockEntity>
{
    @Override
    public void render(AlembicBlockEntity alembic, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        BlockState state = alembic.getBlockState();

        alembic.getCapability(Capabilities.ITEM).map(inv -> inv.getStackInSlot(AlembicBlockEntity.SLOT_ITEM)).filter(item -> !item.isEmpty()).ifPresent(itemStack -> {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.15625F, 0.5F);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(RenderHelpers.rotateDegreesX(90f));

            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffer, 0);

            poseStack.popPose();
        });
    }
}
