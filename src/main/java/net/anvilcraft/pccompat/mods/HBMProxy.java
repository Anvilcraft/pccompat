package net.anvilcraft.pccompat.mods;

import com.hbm.blocks.ModBlocks;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterHBM;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.recipe.ShapelessOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityHBMConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityHBMProducer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class HBMProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("HBM", "HE", 4000, "HE/t")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPowerConverterHBM(this),
            ItemBlockPowerConverter.class,
            "power_converter_hbm"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(TileEntityHBMConsumer.class, "hbm_consumer");
        GameRegistry.registerTileEntity(TileEntityHBMProducer.class, "hbm_producer");
    }

    @Override
    public void registerRecipes() {
        ItemStack cable = new ItemStack(ModBlocks.red_cable);

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("G G", " C ", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('C', cable)
            .output(new ItemStack(blockPowerConverter, 1, 0))
            .register();

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .ingredient(new ItemStack(blockPowerConverter, 1, 0))
            .output(new ItemStack(blockPowerConverter, 1, 1))
            .register();

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .ingredient(new ItemStack(blockPowerConverter, 1, 1))
            .output(new ItemStack(blockPowerConverter, 1, 0))
            .register();
    }

    @Override
    public String getModPrefix() {
        return "hbm";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityHBMConsumer() : new TileEntityHBMProducer();
    }
}
