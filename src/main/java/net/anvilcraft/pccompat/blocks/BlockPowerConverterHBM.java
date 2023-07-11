package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.tiles.TileEntityHBMConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityHBMProducer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterHBM extends BlockPowerConverter {
    public BlockPowerConverterHBM() {
        super(2);
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 4; i++) {
            String state = i % 2 == 0 ? "off" : "on";
            String type = i < 2 ? "consumer" : "producer";

            this._icons[i] = reg.registerIcon("pccompat:ue_mv_" + type + "_" + state); //TODO: create unique textures for HBM
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 0 ? new TileEntityHBMConsumer() : new TileEntityHBMProducer();
    }
}
