package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.tiles.TileEntityMagneticraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityMagneticraftProducer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterMagneticraft extends BlockPowerConverter {
    public BlockPowerConverterMagneticraft() {
        super(2); // 1 Consumer, 1 Producer
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        Utils.registerStandardPowerConverterBlockIcons(reg, this._icons, "mc");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 0 ? new TileEntityMagneticraftConsumer()
                         : new TileEntityMagneticraftProducer();
    }
}
