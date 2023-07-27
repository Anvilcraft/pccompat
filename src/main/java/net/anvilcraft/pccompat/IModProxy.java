package net.anvilcraft.pccompat;

import net.minecraft.tileentity.TileEntity;

public interface IModProxy {
    public void registerPowerSystem();
    public void registerBlocks();
    public void registerTiles();
    public void registerRecipes();

    public default int getMetaCount() {
        return 2;
    }

    public String getModPrefix();
    public TileEntity createTileEntity(int meta);
}
