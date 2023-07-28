package net.anvilcraft.pccompat.mods;

import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import mrtjp.projectred.ProjectRedExpansion;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityProjectRedConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityProjectRedProducer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ProjectRedProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("ProjectRed", "PR", 7000, "W")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_project_red"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityProjectRedConsumer.class, "project_red_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityProjectRedProducer.class, "project_red_producer"
        );
    }

    @Override
    public void registerRecipes() {
        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .pattern("G G", " B ", "G G")
            .ingredient('G', "ingotGold")
            .ingredient('B', new ItemStack(ProjectRedExpansion.machine2(), 1, 5))
            .output(blockPowerConverter)
            .register();

        Utils.registerConversionRecipes(blockPowerConverter, 0, 1);
    }

    @Override
    public String getModPrefix() {
        return "pr";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityProjectRedConsumer()
                         : new TileEntityProjectRedProducer();
    }
}
