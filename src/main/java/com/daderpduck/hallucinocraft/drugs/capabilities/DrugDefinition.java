package com.daderpduck.hallucinocraft.drugs.capabilities;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.util.ItemDefinition;

public class DrugDefinition extends ItemDefinition
{
    private final DrugData data;

    public DrugDefinition(ResourceLocation id, JsonObject json)
    {
        super(id, json);
        this.data = DrugData.read(json);
    }

    public DrugDefinition(ResourceLocation id, FriendlyByteBuf buffer)
    {
        super(id, Ingredient.fromNetwork(buffer));
        this.data = DrugData.decode(buffer);
    }

    public void encode(FriendlyByteBuf buffer)
    {
        ingredient.toNetwork(buffer);
        data.encode(buffer);
    }

    public DrugData getData()
    {
        return data;
    }
}
