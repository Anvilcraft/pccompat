package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.tiles.TileEntityHBMConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityHBMProducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterHBM extends BlockPowerConverter {
    public BlockPowerConverterHBM() {
        super(2);
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 0 ? new TileEntityHBMProducer() : new TileEntityHBMConsumer();
    }
}
