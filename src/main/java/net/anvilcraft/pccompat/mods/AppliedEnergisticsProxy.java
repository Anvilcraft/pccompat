package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterAppliedEnergistics;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverterAppliedEnergistics;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsProducer;
import net.minecraft.block.Block;

public class AppliedEnergisticsProxy implements IModProxy {

    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem(
                "Applied Energistics",
                "AE",
                2000,
                "AE"
            )
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(blockPowerConverter = new BlockPowerConverterAppliedEnergistics(), ItemBlockPowerConverterAppliedEnergistics.class, "power_converter_applied_energistics");
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(TileEntityAppliedEnergisticsConsumer.class, "applied_energistics_consumer");
        GameRegistry.registerTileEntity(TileEntityAppliedEnergisticsProducer.class, "applied_energistics_producer");
    }

    @Override
    public void registerRecipes() {
        
    }
    
}
