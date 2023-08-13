package com.daderpduck.hallucinocraft.util;

import com.daderpduck.hallucinocraft.Hallucinocraft;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class HCHelpers
{
    /**
     * If the current environment is a bootstrapped one, i.e. one outside the transforming class loader, such as /gradlew test launch
     */
    public static final boolean BOOTSTRAP_ENVIRONMENT = detectBootstrapEnvironment();

    /**
     * Default {@link ResourceLocation}, except with a TFC namespace
     */
    public static ResourceLocation identifier(String name)
    {
        return new ResourceLocation(Hallucinocraft.MOD_ID, name);
    }

    @SuppressWarnings("ConstantConditions")
    public static <T> Capability<T> capability(CapabilityToken<T> token)
    {
        return BOOTSTRAP_ENVIRONMENT ? null : CapabilityManager.get(token);
    }

    /**
     * Use over invoking the constructor, as Mojang refactors this in 1.19
     */
    public static TextComponent literal(String literalText)
    {
        return new TextComponent(literalText);
    }

    /**
     * Detect if we are in a bootstrapped environment - one where transforming and many MC/Forge mechanics are not properly setup
     * This detects i.e. when running from /gradlew test, and some things have to be avoided (for instance, invoking Forge registry methods)
     */
    private static boolean detectBootstrapEnvironment()
    {
        return System.getProperty("forge.enabledGameTestNamespaces") == null && detectTestSourcesPresent();
    }

    /**
     * Detect if test sources are present, if we're running from a environment which includes TFC's test sources
     * This can happen through a gametest launch, TFC dev launch (since we include test sources), or through gradle test
     */
    private static boolean detectTestSourcesPresent()
    {
        try
        {
            Class.forName("net.dries007.tfc.TestMarker");
            return true;
        }
        catch (ClassNotFoundException e) { /* Guess not */ }
        return false;
    }
}
