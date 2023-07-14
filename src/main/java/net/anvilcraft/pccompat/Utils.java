package net.anvilcraft.pccompat;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class Utils {
    public static void registerStandardPowerConverterBlockIcons(
        IIconRegister reg, IIcon[] icons, String modPrefix
    ) {
        for (int i = 0; i < 4; i++) {
            String state = i % 2 == 0 ? "off" : "on";
            String type = i < 2 ? "consumer" : "producer";

            icons[i]
                = reg.registerIcon("pccompat:" + modPrefix + "_" + type + "_" + state);
        }
    }
}
