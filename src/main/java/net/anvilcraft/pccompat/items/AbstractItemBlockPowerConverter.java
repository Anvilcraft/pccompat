package net.anvilcraft.pccompat.items;

import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class AbstractItemBlockPowerConverter extends ItemBlock {
    public AbstractItemBlockPowerConverter(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public abstract String getModPrefix();

    public int getSubItemCount() {
        return 2;
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "pccompat:power_converter_" + this.getModPrefix() + "_"
            + stack.getItemDamage();
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item alec1, CreativeTabs alec2, List list) {
        IntStream.range(0, this.getSubItemCount())
            .mapToObj((i) -> new ItemStack(this, 1, i))
            .forEach(list::add);
    }
}
