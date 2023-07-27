package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.tiles.TileEntityRedPowerConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityRedPowerProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class RedPowerProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Bluetricity", "BT", 7000, "W")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_red_power"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityRedPowerConsumer.class, "red_power_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityRedPowerProducer.class, "red_power_producer"
        );
    }

    @Override
    public void registerRecipes() {}

    @Override
    public String getModPrefix() {
        return "rp";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityRedPowerConsumer()
                         : new TileEntityRedPowerProducer();
    }
}
