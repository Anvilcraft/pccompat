package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterGregTech;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityGregTechConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityGregTechProducer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class GregTechProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem(
                "Electricity Units",
                "EU",
                4000,
                new String[] { "LV", "MV", "HV", "EV" },
                new int[] { 32, 128, 512, 2048 },
                "EU/t"
            )
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPowerConverterGregTech(this),
            ItemBlockPowerConverter.class,
            "power_converter_gregtech"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityGregTechConsumer.class, "gregtech_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityGregTechProducer.class, "gregtech_producer"
        );
    }

    @Override
    public void registerRecipes() {
        for (int i = 0; i < 4; i++) {
            new RecipeBuilder(new ShapedOreRecipeAdapter())
                .pattern("G G", " T ", "G G")
                .ingredient('G', "ingotGold")
                .ingredient('T', new ItemStack(GregTech_API.sBlockMachines, 1, 21 + i))
                .output(new ItemStack(blockPowerConverter, 1, i * 2))
                .register();

            Utils.registerConversionRecipes(blockPowerConverter, i * 2, i * 2 + 1);
        }
    }

    @Override
    public int getMetaCount() {
        return 8;
    }

    @Override
    public String getModPrefix() {
        return "gt";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta % 2 != 0 ? new TileEntityGregTechProducer(meta / 2)
                             : new TileEntityGregTechConsumer(meta / 2);
    }
}
