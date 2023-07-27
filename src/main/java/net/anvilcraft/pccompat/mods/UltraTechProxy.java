package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.tiles.TileEntityUltraTechConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityUltraTechProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class UltraTechProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Quantum Power", "QP", 20000, "QP/t")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_ultra_tech"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityUltraTechConsumer.class, "ultra_tech_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityUltraTechProducer.class, "ultra_tech_producer"
        );
    }

    @Override
    public void registerRecipes() {}

    @Override
    public String getModPrefix() {
        return "ut";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityUltraTechConsumer()
                         : new TileEntityUltraTechProducer();
    }
}
