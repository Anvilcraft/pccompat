package net.anvilcraft.pccompat.items;

import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPowerConverterUniversalElectricity extends ItemBlock {
    public ItemBlockPowerConverterUniversalElectricity(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();

        return "pccompat:power_converter_ue_" + meta;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item alec1, CreativeTabs alec2, List list) {
        IntStream.range(0, 8)
            .mapToObj((i) -> new ItemStack(this, 1, i))
            .forEach(list::add);
    }
}
