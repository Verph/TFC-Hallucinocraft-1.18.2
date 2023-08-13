package com.daderpduck.hallucinocraft.fluid;

import java.util.Locale;

public enum HCSimpleFluid
{
    UNBREWED_CANNABIS_TEA(0xFF956C48),
    CANNABIS_TEA(0xFFE79642),
    UNBREWED_COCA_TEA(0xFFBCCC5F),
    COCA_TEA(0xFF90A716),
    OPIUM(0xFF937A7A),
    MORPHINE(0xFF945B43),
    SOUL_RESTER(0xFF6A5244),
    SOUL_WRENCHER(0xFF01A7AC),
    COFFEE(0xFF562C00),
    WORT(0xFF654321);

    private final String id;
    private final int color;

    HCSimpleFluid(int color)
    {
        this.id = name().toLowerCase(Locale.ROOT);
        this.color = color;
    }

    public String getId()
    {
        return id;
    }

    public int getColor()
    {
        return color;
    }
}
