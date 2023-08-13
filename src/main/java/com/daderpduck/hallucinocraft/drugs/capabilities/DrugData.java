package com.daderpduck.hallucinocraft.drugs.capabilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.drugs.Drugs;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record DrugData(ArrayList<DrugInstance> drugs)
{
    public static final DrugData EMPTY = new DrugData(null);

    public static DrugData create(ArrayList<DrugInstance> drugs)
    {
        return new DrugData(drugs);
    }

    public ArrayList<DrugInstance> getDrugs()
    {
        return drugs;
    }

    public static DrugData read(JsonObject json)
    {
        ArrayList<DrugInstance> drugInsts = new ArrayList<DrugInstance>();
        if (json.has("drugs"))
        {
            JsonArray array = JsonHelpers.getAsJsonArray(json, "drugs");
            for (JsonElement e : array)
            {
                final JsonObject effectJson = JsonHelpers.convertToJsonObject(e, "drugs");
                final Drug drug = JsonHelpers.getRegistryEntry(effectJson, "type", Drugs.DrugRegistry);
                final int delayTime = JsonHelpers.getAsInt(effectJson, "delayTime", 20);
                final float potency = (float) JsonHelpers.getAsDouble(effectJson, "potency", 0);
                final int duration = JsonHelpers.getAsInt(effectJson, "duration", 1);
                final int timeActive = JsonHelpers.getAsInt(effectJson, "timeActive", 0);

                drugInsts.add(new DrugInstance(drug, delayTime, potency, duration, timeActive));
            }
        }
        return DrugData.create(drugInsts);
    }

    public static DrugData read(CompoundTag nbt)
    {
        ArrayList<DrugInstance> drugInsts = new ArrayList<DrugInstance>();

        for (int i = 0; i < nbt.size(); i++)
        {
            CompoundTag drug = (CompoundTag) nbt.get("drug" + i);

            drugInsts.add(new DrugInstance(Drugs.DrugMap.get(drug.getString("type")).get(), drug.getInt("delayTime"), drug.getFloat("potency"), drug.getInt("duration"), drug.getInt("timeActive")));
        }

        return new DrugData(drugInsts);
    }

    public CompoundTag write()
    {
        final CompoundTag nbt = new CompoundTag();

        int i = 0;
        for (DrugInstance drugInst : drugs)
        {
            CompoundTag drug = new CompoundTag();
            drug.putString("type", drugInst.toName());
            drug.putInt("duration", drugInst.getDuration());
            drug.putFloat("potency", drugInst.getPotency());
            drug.putInt("delayTime", drugInst.getDelayTime());
            drug.putInt("timeActive", drugInst.getTimeActive());
            nbt.put("drug" + i, drug);
            i++;
        }
        return nbt;
    }

    public static DrugData decode(FriendlyByteBuf buffer)
    {
        List<DrugInstance> drugsList = buffer.readList((buf) -> {
            String drug = buf.readUtf();
            int duration = buf.readInt();
            float potency = buf.readFloat();
            int delayTime = buf.readInt();
            int timeActive = buf.readInt();
            return new DrugInstance(Drugs.DrugMap.get(drug).get(), delayTime, potency, duration, timeActive);
        });

        ArrayList<DrugInstance> drugs = new ArrayList<DrugInstance>(drugsList);

        return DrugData.create(drugs);
    }

    public void encode(FriendlyByteBuf buffer)
    {
        Collection<DrugInstance> drugCol = drugs;

        buffer.writeCollection(drugCol, (buf, fer) -> {
            buf.writeUtf(fer.toName());
            buf.writeInt(fer.getDuration());
            buf.writeFloat(fer.getPotency());
            buf.writeInt(fer.getDelayTime());
            buf.writeInt(fer.getTimeActive());
        });
    }
}
