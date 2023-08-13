package com.daderpduck.hallucinocraft.client.screen;

import com.daderpduck.hallucinocraft.blocks.entities.AlembicBlockEntity;
import com.daderpduck.hallucinocraft.container.AlembicContainer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.screen.BlockEntityScreen;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Tooltips;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class AlembicScreen extends BlockEntityScreen<AlembicBlockEntity, AlembicContainer>
{
    private static final int MAX_RECIPE_NAME_LENGTH = 100;

    public static final ResourceLocation BACKGROUND = Helpers.identifier("textures/gui/barrel.png");

    public AlembicScreen(AlembicContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, BACKGROUND);
        inventoryLabelY += 12;
        imageHeight += 12;
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
        super.renderLabels(poseStack, mouseX, mouseY);

        /*BarrelRecipe recipe = blockEntity.getRecipe();
        if (recipe != null)
        {
            FormattedText resultText = recipe.getTranslationComponent();
            if (font.width(resultText) > MAX_RECIPE_NAME_LENGTH)
            {
                int line = 0;
                for (FormattedCharSequence text : font.split(resultText, MAX_RECIPE_NAME_LENGTH))
                {
                    font.draw(poseStack, text, 70 + Math.floorDiv(MAX_RECIPE_NAME_LENGTH - font.width(text), 2), titleLabelY + (line * font.lineHeight), 0x404040);
                    line++;
                }
            }
            else
            {
                font.draw(poseStack, resultText.getString(), 70 + Math.floorDiv(MAX_RECIPE_NAME_LENGTH - font.width(resultText), 2), 61, 0x404040);
            }
        }*/
        //String date = ICalendar.getTimeAndDate(Calendars.CLIENT.ticksToCalendarTicks(blockEntity.getSealedTick()), Calendars.CLIENT.getCalendarDaysInMonth()).getString();
        font.draw(poseStack, "date", imageWidth / 2f - font.width("date") / 2f, 74, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);
        blockEntity.getCapability(Capabilities.FLUID).ifPresent(fluidHandler -> {
            FluidStack fluidStack = fluidHandler.getFluidInTank(0);
            if (!fluidStack.isEmpty())
            {
                final TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(fluidStack);
                final int fillHeight = (int) Math.ceil((float) 50 * fluidStack.getAmount() / (float) TFCConfig.SERVER.barrelCapacity.get());

                RenderHelpers.fillAreaWithSprite(poseStack, sprite, leftPos + 8, topPos + 70 - fillHeight, 16, fillHeight, 16, 16);

                resetToBackgroundSprite();
            }
        });

        blit(poseStack, getGuiLeft() + 7, getGuiTop() + 19, 176, 0, 18, 52);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY)
    {
        super.renderTooltip(poseStack, mouseX, mouseY);
        final int relX = mouseX - getGuiLeft();
        final int relY = mouseY - getGuiTop();

        if (relX >= 7 && relY >= 19 && relX < 25 && relY < 71)
        {
            blockEntity.getCapability(Capabilities.FLUID).ifPresent(fluidHandler -> {
                FluidStack fluid = fluidHandler.getFluidInTank(0);
                if (!fluid.isEmpty())
                {
                    renderTooltip(poseStack, Tooltips.fluidUnitsOf(fluid), mouseX, mouseY);
                }
            });
        }
    }
}