package net.anvilcraft.pccompat;

import java.lang.reflect.Field;
import java.util.HashSet;

import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import covers1624.powerconverters.init.PowerSystems;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.anvilcraft.pccompat.mods.AppliedEnergisticsProxy;
import net.anvilcraft.pccompat.mods.HBMProxy;
import net.anvilcraft.pccompat.mods.RedPowerProxy;
import net.anvilcraft.pccompat.mods.UniversalElectricityProxy;

@Mod(
    modid = "pccompat",
    name = "PC Compat",
    version = "0.1.0",
    dependencies
    = "required-after:PowerConverters3;after:basiccomponents;after:appliedenergistics2;after:hbm;after:RedPowerCore"
)
public class PCCompat {
    public static HashSet<IModProxy> mods = new HashSet<>();

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent ev) {
        collectMods();
        try {
            // Fix IC2 scale "Ammount" due to mod outragous developer incompetence
            Field field = PowerSystem.class.getDeclaredField("scaleAmmount");
            field.setAccessible(true);
            field.setInt(PowerSystems.powerSystemIndustrialCraft, 4000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (IModProxy mp : mods) {
            mp.registerPowerSystem();
            mp.registerBlocks();
            mp.registerTiles();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
        for (IModProxy mp : mods)
            mp.registerRecipes();
    }

    private static void collectMods() {
        if (Loader.isModLoaded("basiccomponents"))
            mods.add(new UniversalElectricityProxy());
        if (Loader.isModLoaded("appliedenergistics2"))
            mods.add(new AppliedEnergisticsProxy());
        if (Loader.isModLoaded("hbm"))
            mods.add(new HBMProxy());
        if (Loader.isModLoaded("RedPowerCore"))
            mods.add(new RedPowerProxy());
    }
}
