package net.anvilcraft.pccompat.items;

import net.minecraft.block.Block;

public class ItemBlockPowerConverterUniversalElectricity extends AbstractItemBlockPowerConverter {
    public ItemBlockPowerConverterUniversalElectricity(Block block) {
        super(block);
    }

    @Override
    public int getSubItemCount() {
        return 8;
    }

    @Override
    public String getModPrefix() {
        return "ue";
    }
}
