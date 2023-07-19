package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterRailcraft;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverterRailcraft;
import net.anvilcraft.pccompat.tiles.TileEntityRailcraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityRailcraftProducer;
import net.minecraft.block.Block;

public class RailcraftProxy implements IModProxy {

    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Railcraft", "Charge", 4000, "C")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPowerConverterRailcraft(),
            ItemBlockPowerConverterRailcraft.class,
            "power_converter_railcraft"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityRailcraftConsumer.class, "railcraft_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityRailcraftProducer.class, "railcraft_producer"
        );
    }

    @Override
    public void registerRecipes() {

    }
    
}
