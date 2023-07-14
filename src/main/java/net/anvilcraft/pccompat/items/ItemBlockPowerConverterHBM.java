package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterHBM extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterHBM(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "hbm";
    }
}
