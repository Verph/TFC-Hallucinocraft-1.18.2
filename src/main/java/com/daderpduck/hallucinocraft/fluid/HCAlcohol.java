package com.daderpduck.hallucinocraft.fluid;

import java.util.Locale;

public enum HCAlcohol
{
    TEQUILA(0xFFDCDCDC);

    private final String id;
    private final int color;

    HCAlcohol(int color)
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
