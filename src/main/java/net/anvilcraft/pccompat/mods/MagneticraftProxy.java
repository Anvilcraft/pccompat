package net.anvilcraft.pccompat.mods;

import com.cout970.magneticraft.ManagerBlocks;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityMagneticraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityMagneticraftProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class MagneticraftProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Magneticraft", "MC", 100, "W")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_magneticraft"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityMagneticraftConsumer.class, "magneticraft_consumber"
        );
        GameRegistry.registerTileEntity(
            TileEntityMagneticraftProducer.class, "magneticraft_producer"
        );
    }

    @Override
    public void registerRecipes() {
        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("G G", " B ", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('B', ManagerBlocks.battery)
            .output(blockPowerConverter)
            .register();

        Utils.registerConversionRecipes(blockPowerConverter, 0, 1);
    }

    @Override
    public String getModPrefix() {
        return "mc";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityMagneticraftConsumer()
                         : new TileEntityMagneticraftProducer();
    }
}
