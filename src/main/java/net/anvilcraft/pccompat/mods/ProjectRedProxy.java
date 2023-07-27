package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.tiles.TileEntityProjectRedConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityProjectRedProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class ProjectRedProxy implements IModProxy {
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
            "power_converter_project_red"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityProjectRedConsumer.class, "project_red_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityProjectRedProducer.class, "project_red_producer"
        );
    }

    @Override
    public void registerRecipes() {}

    @Override
    public String getModPrefix() {
        return "pr";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityProjectRedConsumer()
                         : new TileEntityProjectRedProducer();
    }
}
