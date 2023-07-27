package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterMagneticraft;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverterMagneticraft;
import net.anvilcraft.pccompat.tiles.TileEntityMagneticraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityMagneticraftProducer;
import net.minecraft.block.Block;

public class MagneticraftProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Joules", "J", 100, "W")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPowerConverterMagneticraft(),
            ItemBlockPowerConverterMagneticraft.class,
            "power_converter_magneticraft"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityMagneticraftConsumer.class, "magneticraft_consumber"
        );
        GameRegistry.registerTileEntity(
            TileEntityMagneticraftProducer.class, "magneticraft_producer"
        );
    }

    @Override
    public void registerRecipes() {}
}
