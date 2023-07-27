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
import net.anvilcraft.pccompat.mods.GregTechProxy;
import net.anvilcraft.pccompat.mods.HBMProxy;
import net.anvilcraft.pccompat.mods.MagneticraftProxy;
import net.anvilcraft.pccompat.mods.ProjectRedProxy;
import net.anvilcraft.pccompat.mods.RailcraftProxy;
import net.anvilcraft.pccompat.mods.RedPowerProxy;
import net.anvilcraft.pccompat.mods.UltraTechProxy;
import net.anvilcraft.pccompat.mods.UniversalElectricityProxy;

@Mod(
    modid = "pccompat",
    name = "PC Compat",
    version = "0.1.0",
    dependencies
    = "required-after:PowerConverters3;after:basiccomponents;after:appliedenergistics2;after:hbm;after:RedPowerCore;after:ProjRed|Expansion;after:gregtech;after:UltraTech;after:Railcraft;after:Magneticraft"
)
public class PCCompat {
    public static HashSet<IModProxy> mods = new HashSet<>();

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent ev) {
        collectMods();
        try {
            // Fix IC2 scale "Ammount" due to outragous mod developer incompetence
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
        if (Loader.isModLoaded("ProjRed|Expansion"))
            mods.add(new ProjectRedProxy());
        if (Loader.isModLoaded("gregtech"))
            mods.add(new GregTechProxy());
        if (Loader.isModLoaded("UltraTech"))
            mods.add(new UltraTechProxy());
        if (Loader.isModLoaded("Railcraft"))
            mods.add(new RailcraftProxy());
        if (Loader.isModLoaded("Magneticraft"))
            mods.add(new MagneticraftProxy());
    }
}
