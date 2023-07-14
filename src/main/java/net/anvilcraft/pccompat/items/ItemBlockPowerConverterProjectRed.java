package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterProjectRed extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterProjectRed(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "pr";
    }
}
