package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterUltraTech extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterUltraTech(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "ut";
    }
}
