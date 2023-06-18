package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.tiles.TileEntityRedPowerConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityRedPowerProducer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterRedPower extends BlockPowerConverter {
    public BlockPowerConverterRedPower() {
        super(2); // 1 Consumer, 1 Producer
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 4; i++) {
            String state = i % 2 == 0 ? "off" : "on";
            String type = i < 2 ? "consumer" : "producer";

            this._icons[i] = reg.registerIcon("pccompat:rp_" + type + "_" + state);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        switch (metadata) {
            case 0:
                return new TileEntityRedPowerConsumer();

            case 1:
                return new TileEntityRedPowerProducer();

            default:
                return null;
        }
    }
}
