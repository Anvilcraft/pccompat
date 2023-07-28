package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.railcraft.common.blocks.RailcraftBlocks;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityRailcraftConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityRailcraftProducer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class RailcraftProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Railcraft", "RC", 4000, "C/t")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_railcraft"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityRailcraftConsumer.class, "railcraft_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityRailcraftProducer.class, "railcraft_producer"
        );
    }

    @Override
    public void registerRecipes() {
        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("G G", " W ", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('W', RailcraftBlocks.getBlockMachineDelta())
            .output(blockPowerConverter).register();

        Utils.registerConversionRecipes(blockPowerConverter, 0, 1);
    }

    @Override
    public String getModPrefix() {
        return "rc";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityRailcraftConsumer()
                         : new TileEntityRailcraftProducer();
    }
}
