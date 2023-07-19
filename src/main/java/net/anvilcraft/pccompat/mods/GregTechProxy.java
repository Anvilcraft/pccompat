package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterGregTech;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverterGregTech;
import net.anvilcraft.pccompat.tiles.TileEntityGregTechConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityGregTechProducer;
import net.minecraft.block.Block;

public class GregTechProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem(
                "Electricity Units",
                "EU",
                4000,
                new String[] { "LV", "MV", "HV", "EV" },
                new int[] { 32, 128, 512, 2048 },
                "EU/t"
            )
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPowerConverterGregTech(),
            ItemBlockPowerConverterGregTech.class,
            "power_converter_gregtech"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityGregTechConsumer.class, "gregtech_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityGregTechProducer.class, "gregtech_producer"
        );
    }

    @Override
    public void registerRecipes() {}
}
