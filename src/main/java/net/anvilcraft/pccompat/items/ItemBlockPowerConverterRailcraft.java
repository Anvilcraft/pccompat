package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterRailcraft extends AbstractItemBlockPowerConverter {

    public ItemBlockPowerConverterRailcraft(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "rc";
    }
    
}
