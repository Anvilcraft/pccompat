package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.tiles.TileEntityElectriCraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityElectriCraftProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class ElectriCraftProxy implements IModProxy {
    public static final double ACTUAL_SCALE_AMOUNT = 112.0 / 20.0;

    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem(
                // Scale "Ammount" determined experimentally
                "Joules",
                "J",
                120 / 20,
                "W"
            )
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_electricraft"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityElectriCraftConsumer.class, "electricraft_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityElectriCraftProducer.class, "electricraft_producer"
        );
    }

    @Override
    public void registerRecipes() {}

    @Override
    public String getModPrefix() {
        return "ec";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityElectriCraftConsumer()
                         : new TileEntityElectriCraftProducer();
    }
}
