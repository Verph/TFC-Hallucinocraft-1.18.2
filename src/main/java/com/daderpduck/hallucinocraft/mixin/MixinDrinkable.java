package com.daderpduck.hallucinocraft.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.dries007.tfc.util.Drinkable;
import net.dries007.tfc.util.JsonHelpers;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.drugs.Drugs;

@Mixin(Drinkable.class)
public class MixinDrinkable
{
    @Unique @Final private List<DrugInstance> drugs;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void OnConstruct(ResourceLocation id, JsonObject json, CallbackInfo ci)
    {
        final ImmutableList.Builder<DrugInstance> builderDrugs = new ImmutableList.Builder<>();
        if (json.has("drugs"))
        {
            JsonArray array = JsonHelpers.getAsJsonArray(json, "drugs");
            for (JsonElement e : array)
            {
                final JsonObject effectJson = JsonHelpers.convertToJsonObject(e, "drugs");
                final Drug type = JsonHelpers.getRegistryEntry(effectJson, "type", Drugs.DrugRegistry);
                final int delayTime = JsonHelpers.getAsInt(effectJson, "delayTime", 20);
                final float potency = (float) JsonHelpers.getAsDouble(effectJson, "potency", 0);
                final int duration = JsonHelpers.getAsInt(effectJson, "duration", 1);

                builderDrugs.add(new DrugInstance(type, delayTime, potency, duration));
            }
        }
        this.drugs = builderDrugs.build();
    }

    @Inject(at = @At("TAIL"), method = "onDrink", remap = false)
    public void onDrinkTail(Player player, int mB, CallbackInfo ci)
    {
        for (DrugInstance drug : drugs)
        {
            Drug.addDrug(player, drug);
        }
    }
}
