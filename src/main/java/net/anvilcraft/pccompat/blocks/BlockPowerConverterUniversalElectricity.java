package net.anvilcraft.pccompat.blocks;

import net.anvilcraft.pccompat.IModProxy;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockPowerConverterUniversalElectricity extends BlockPCCConverter {
    public BlockPowerConverterUniversalElectricity(IModProxy proxy) {
        super(proxy);
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

            String texture_name = "pccompat:" + this.proxy.getModPrefix() + "_" + voltage
                + "_" + type + "_" + state;
            this._icons[i] = reg.registerIcon(texture_name);
        }
    }
}
