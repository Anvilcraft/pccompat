package net.anvilcraft.pccompat.mods;

import com.google.common.base.Optional;

import appeng.api.AEApi;
import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.blocks.BlockPowerConverterAppliedEnergistics;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverterAppliedEnergistics;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.recipe.ShapelessOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsProducer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class AppliedEnergisticsProxy implements IModProxy {

    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem(
                "Applied Energistics",
                "AE",
                2000,
                "AE"
            )
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(blockPowerConverter = new BlockPowerConverterAppliedEnergistics(), ItemBlockPowerConverterAppliedEnergistics.class, "power_converter_applied_energistics");
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(TileEntityAppliedEnergisticsConsumer.class, "applied_energistics_consumer");
        GameRegistry.registerTileEntity(TileEntityAppliedEnergisticsProducer.class, "applied_energistics_producer");
    }

    @Override
    public void registerRecipes() {
        Optional<ItemStack> acceptor = AEApi.instance().definitions().blocks().energyAcceptor().maybeStack(1);
        Optional<ItemStack> cell = AEApi.instance().definitions().blocks().energyCell().maybeStack(1);
        if (acceptor.isPresent() || cell.isPresent()) {
            ItemStack stack = acceptor.isPresent() ? acceptor.get() : cell.get();
            new RecipeBuilder(new ShapedOreRecipeAdapter())
                .pattern("G G", " S ", "G G")
                .ingredient('G', "ingotGold")
                .ingredient('S', stack)
                .output(new ItemStack(blockPowerConverter, 1, 0))
                .register();
        }

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .ingredient(
                new ItemStack(blockPowerConverter, 1, 0)
            )
            .output(
                new ItemStack(blockPowerConverter, 1, 1)
            )
            .register();

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .ingredient(
                new ItemStack(blockPowerConverter, 1, 1)
            )
            .output(new ItemStack(blockPowerConverter, 1, 0))
            .register();
    }
    
}
