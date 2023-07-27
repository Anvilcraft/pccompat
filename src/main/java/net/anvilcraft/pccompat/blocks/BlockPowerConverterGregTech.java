package net.anvilcraft.pccompat.blocks;

import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockPowerConverterGregTech extends BlockPCCConverter {
    public BlockPowerConverterGregTech(IModProxy proxy) {
        super(proxy);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        Utils.registerStandardPowerConverterBlockIcons(reg, this._icons, this.proxy.getModPrefix());

        // TODO: textures for voltage levels
        for (int i = 1; i <= 3; i++) {
            this._icons[i * 4 + 0] = this._icons[0];
            this._icons[i * 4 + 1] = this._icons[1];
            this._icons[i * 4 + 2] = this._icons[2];
            this._icons[i * 4 + 3] = this._icons[3];
        }
    }
}
