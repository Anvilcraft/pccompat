package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterAppliedEnergistics
    extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterAppliedEnergistics(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "ae";
    }
}
