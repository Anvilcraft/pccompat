package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterGregTech extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterGregTech(Block block) {
        super(block);
    }

    @Override
    public int getSubItemCount() {
        return 8;
    }

    @Override
    public String getModPrefix() {
        return "gt";
    }
}
