package net.anvilcraft.pccompat;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterUniversalElectricity;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverterUniversalElectricity;
import net.minecraft.block.Block;

public class PCCBlocks {
    public static Block powerConverterUniversalElectricity;

    public static void register() {
        GameRegistry.registerBlock(
            powerConverterUniversalElectricity
            = new BlockPowerConverterUniversalElectricity(),
            ItemBlockPowerConverterUniversalElectricity.class,
            "power_converter_universal_electrictity"
        );
    }
}
