package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsProducer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterAppliedEnergistics extends BlockPowerConverter {
    public BlockPowerConverterAppliedEnergistics() {
        super(2);
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        Utils.registerStandardPowerConverterBlockIcons(reg, this._icons, "ae");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 0 ? new TileEntityAppliedEnergisticsConsumer()
                         : new TileEntityAppliedEnergisticsProducer();
    }
}
