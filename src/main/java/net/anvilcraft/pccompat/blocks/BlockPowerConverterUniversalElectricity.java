package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.tiles.TileEntityUniversalElectricityConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityUniversalElectricityProducer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockPowerConverterUniversalElectricity extends BlockPowerConverter {
    public IIcon[] icons;

    public BlockPowerConverterUniversalElectricity() {
        super(8); // 4 Consumers, 4 Producers
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 16; i++) {
            String state = i % 2 == 0 ? "off" : "on";
            String type = (i / 2) % 2 == 0 ? "consumer" : "producer";

            String voltage;

            switch (i / 4) {
                case 0:
                    voltage = "lv";
                    break;

                case 1:
                    voltage = "mv";
                    break;

                case 2:
                    voltage = "hv";
                    break;

                default:
                    voltage = "ev";
                    break;
            }

            String texture_name = "pccompat:ue_" + voltage + "_" + type + "_" + state;
            this._icons[i] = reg.registerIcon(texture_name);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta % 2 != 0 ? new TileEntityUniversalElectricityProducer(meta / 2)
                             : new TileEntityUniversalElectricityConsumer(meta / 2);
    }
}
