package net.anvilcraft.pccompat.items;

import java.util.List;
import java.util.stream.IntStream;

import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPowerConverter extends ItemBlock {
    public IModProxy proxy;
    public ItemBlockPowerConverter(Block block) {
        super(block);
        this.proxy = ((BlockPCCConverter) block).proxy;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "pccompat:power_converter_" + this.proxy.getModPrefix() + "_"
            + stack.getItemDamage();
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item alec1, CreativeTabs alec2, List list) {
        IntStream.range(0, this.proxy.getMetaCount())
            .mapToObj((i) -> new ItemStack(this, 1, i))
            .forEach(list::add);
    }
}
