package net.anvilcraft.pccompat;

import java.lang.reflect.Field;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import covers1624.powerconverters.init.PowerSystems;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.tiles.TileEntityUniversalElectricityConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityUniversalElectricityProducer;

@Mod(
    modid = "pccompat",
    name = "PC Compat",
    version = "0.1.0",
    dependencies = "required-after:PowerConverters3"
)
public class PCCompat {
    public static PowerSystem universalElectricityPowerSystem;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent ev) {
        PowerSystemRegistry.registerPowerSystem(
            universalElectricityPowerSystem = new PowerSystem(
                "Universal Electricity",
                "UE",
                400,
                new String[] { "LV", "MV", "HV", "EV" },
                new int[] { 60, 120, 240, 480 },
                "W"
            )
        );

        try {
            Field field = PowerSystem.class.getDeclaredField("scaleAmmount");
            field.setAccessible(true);
            field.setInt(PowerSystems.powerSystemIndustrialCraft, 4000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GameRegistry.registerTileEntity(
            TileEntityUniversalElectricityConsumer.class, "universal_electricity_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityUniversalElectricityProducer.class, "universal_electricity_producer"
        );

        PCCBlocks.register();
    }
}
