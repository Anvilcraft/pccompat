package net.anvilcraft.pccompat.mods;

import basiccomponents.common.BasicComponents;
import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterUniversalElectricity;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.recipe.ShapelessOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityUniversalElectricityConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityUniversalElectricityProducer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class UniversalElectricityProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem(
                "Universal Electricity",
                "UE",
                400,
                new String[] { "LV", "MV", "HV", "EV" },
                new int[] { 60, 120, 240, 480 },
                "W"
            )
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPowerConverterUniversalElectricity(this),
            ItemBlockPowerConverter.class,
            "power_converter_universal_electrictity"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityUniversalElectricityConsumer.class, "universal_electricity_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityUniversalElectricityProducer.class, "universal_electricity_producer"
        );
    }

    @Override
    public void registerRecipes() {
        ItemStack batbox = new ItemStack(BasicComponents.blockMachine, 1, 4);

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("G G", "   ", "GBG")
            .ingredient('G', "ingotGold")
            .ingredient('B', batbox)
            .output(new ItemStack(blockPowerConverter, 1, 0))
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("G G", " B ", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('B', batbox)
            .output(new ItemStack(blockPowerConverter, 1, 2))
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("GBG", "   ", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('B', batbox)
            .output(new ItemStack(blockPowerConverter, 1, 4))
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("GBG", "G G", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('B', batbox)
            .output(new ItemStack(blockPowerConverter, 1, 6))
            .register();

        for (int i = 0; i < 8; i += 2) {
            new RecipeBuilder(new ShapelessOreRecipeAdapter())
                .ingredient(new ItemStack(blockPowerConverter, 1, i))
                .output(new ItemStack(blockPowerConverter, 1, i + 1))
                .register();

            new RecipeBuilder(new ShapelessOreRecipeAdapter())
                .ingredient(new ItemStack(blockPowerConverter, 1, i + 1))
                .output(new ItemStack(blockPowerConverter, 1, i))
                .register();
        }
    }

    @Override
    public int getMetaCount() {
        return 8;
    }

    @Override
    public String getModPrefix() {
        return "ue";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta % 2 != 0 ? new TileEntityUniversalElectricityProducer(meta / 2)
                             : new TileEntityUniversalElectricityConsumer(meta / 2);
    }
}
