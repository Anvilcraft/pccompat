package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterRedPower extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterRedPower(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "rp";
    }
}
