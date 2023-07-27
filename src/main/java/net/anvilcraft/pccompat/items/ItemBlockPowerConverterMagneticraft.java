package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterMagneticraft extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterMagneticraft(Block block) {
        super(block);
    }

    @Override
    public String getModPrefix() {
        return "mc";
    }
}
