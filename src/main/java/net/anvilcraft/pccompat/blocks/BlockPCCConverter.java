package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPCCConverter extends BlockPowerConverter {
    public IModProxy proxy;

    public BlockPCCConverter(IModProxy proxy) {
        super(proxy.getMetaCount());
        this.proxy = proxy;
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        Utils.registerStandardPowerConverterBlockIcons(
            reg, this._icons, this.proxy.getModPrefix()
        );
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return this.proxy.createTileEntity(meta);
    }
}
