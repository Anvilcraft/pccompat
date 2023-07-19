package net.anvilcraft.pccompat.blocks;

import covers1624.powerconverters.block.BlockPowerConverter;
import covers1624.powerconverters.gui.PCCreativeTab;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.tiles.TileEntityGregTechConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityGregTechProducer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterGregTech extends BlockPowerConverter {
    public BlockPowerConverterGregTech() {
        super(8); // 4 Consumers, 4 Producers
        this.setCreativeTab(PCCreativeTab.tab);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        Utils.registerStandardPowerConverterBlockIcons(reg, this._icons, "gt");

        // TODO: textures for voltage levels
        for (int i = 1; i <= 3; i++) {
            this._icons[i * 4 + 0] = this._icons[0];
            this._icons[i * 4 + 1] = this._icons[1];
            this._icons[i * 4 + 2] = this._icons[2];
            this._icons[i * 4 + 3] = this._icons[3];
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta % 2 != 0 ? new TileEntityGregTechProducer(meta / 2)
                             : new TileEntityGregTechConsumer(meta / 2);
    }
}
