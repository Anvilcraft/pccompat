package net.anvilcraft.pccompat.blocks;

import net.anvilcraft.pccompat.IModProxy;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockPowerConverterHBM extends BlockPCCConverter {
    public BlockPowerConverterHBM(IModProxy proxy) {
        super(proxy);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 4; i++) {
            String state = i % 2 == 0 ? "off" : "on";
            String type = i < 2 ? "consumer" : "producer";

            this._icons[i] = reg.registerIcon(
                "pccompat:ue_mv_" + type + "_" + state
            ); //TODO: create unique textures for HBM
        }
    }
}
