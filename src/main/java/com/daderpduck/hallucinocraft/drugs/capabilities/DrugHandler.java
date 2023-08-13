package com.daderpduck.hallucinocraft.drugs.capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;

public class DrugHandler implements ICapabilitySerializable<CompoundTag>, IDrug
{
    private final LazyOptional<IDrug> capability;
    protected DrugData data;

    public DrugHandler(DrugData data)
    {
        this.data = data;
        this.capability = LazyOptional.of(() -> this);
    }

    @Override
    public DrugData getData()
    {
        return data;
    }

    @Override
    public List<DrugInstance> getDrugs()
    {
        return data.getDrugs();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        if (cap == DrugCapability.CAPABILITY)
        {
            return capability.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.put("drugData", data.write());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        data = DrugData.read(nbt.getCompound("drugData"));
    }

    /**
     * Convenience class for dynamic food handlers
     */
    public static class Dynamic extends DrugHandler
    {
        public Dynamic()
        {
            super(DrugData.EMPTY);
        }

        public void setFood(DrugData data)
        {
            this.data = data;
        }
    }
}
