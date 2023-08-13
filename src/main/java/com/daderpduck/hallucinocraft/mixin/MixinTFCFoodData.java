package com.daderpduck.hallucinocraft.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.dries007.tfc.common.capabilities.food.TFCFoodData;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.drugs.capabilities.DrugCapability;
import com.daderpduck.hallucinocraft.drugs.capabilities.DrugData;
import com.daderpduck.hallucinocraft.drugs.capabilities.IDrug;

@Mixin(TFCFoodData.class)
public class MixinTFCFoodData
{
    @Shadow @Final private Player sourcePlayer;

    @Inject(at = @At("TAIL"), method = "eat", remap = false)
    private void eatDrug(Item maybeFood, ItemStack stack, @Nullable LivingEntity entity, CallbackInfo CI)
    {
        stack.getCapability(DrugCapability.CAPABILITY).ifPresent(this::eat);
    }

    public void eat(IDrug drug)
    {
        final DrugData data = drug.getData();
        for (DrugInstance drugInst : data.getDrugs())
        {
            Drug.addDrug(sourcePlayer, drugInst);
        }
    }
}
