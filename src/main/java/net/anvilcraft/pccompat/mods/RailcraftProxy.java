package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.tiles.TileEntityRailcraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityRailcraftProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class RailcraftProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Railcraft", "Charge", 4000, "C/t")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
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
    public void registerRecipes() {}

    @Override
    public String getModPrefix() {
        return "rc";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityRailcraftConsumer()
                         : new TileEntityRailcraftProducer();
    }
}
