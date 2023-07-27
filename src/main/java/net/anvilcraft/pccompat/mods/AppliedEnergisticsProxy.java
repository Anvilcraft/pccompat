package net.anvilcraft.pccompat.mods;

import com.google.common.base.Optional;

import appeng.api.AEApi;
import covers1624.powerconverters.api.registry.PowerSystemRegistry;
import covers1624.powerconverters.api.registry.PowerSystemRegistry.PowerSystem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.pccompat.IModProxy;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.blocks.BlockPCCConverter;
import net.anvilcraft.pccompat.items.ItemBlockPowerConverter;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapedOreRecipeAdapter;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsConsumer;
import net.anvilcraft.pccompat.tiles.TileEntityAppliedEnergisticsProducer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class AppliedEnergisticsProxy implements IModProxy {
    public static PowerSystem powerSystem;
    public static Block blockPowerConverter;

    @Override
    public void registerPowerSystem() {
        PowerSystemRegistry.registerPowerSystem(
            powerSystem = new PowerSystem("Applied Energistics", "AE", 2000, "AE/t")
        );
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(
            blockPowerConverter = new BlockPCCConverter(this),
            ItemBlockPowerConverter.class,
            "power_converter_applied_energistics"
        );
    }

    @Override
    public void registerTiles() {
        GameRegistry.registerTileEntity(
            TileEntityAppliedEnergisticsConsumer.class, "applied_energistics_consumer"
        );
        GameRegistry.registerTileEntity(
            TileEntityAppliedEnergisticsProducer.class, "applied_energistics_producer"
        );
    }

    @Override
    public void registerRecipes() {
        Optional<ItemStack> acceptor
            = AEApi.instance().definitions().blocks().energyAcceptor().maybeStack(1);
        Optional<ItemStack> cell
            = AEApi.instance().definitions().blocks().energyCell().maybeStack(1);
        if (acceptor.isPresent() || cell.isPresent()) {
            ItemStack stack = acceptor.or(cell::get);
            new RecipeBuilder(new ShapedOreRecipeAdapter())
                .pattern("G G", " S ", "G G")
                .ingredient('G', "ingotGold")
                .ingredient('S', stack)
                .output(blockPowerConverter)
                .register();
        }

        Utils.registerConversionRecipes(blockPowerConverter, 0, 1);
    }

    @Override
    public String getModPrefix() {
        return "ae";
    }

    @Override
    public TileEntity createTileEntity(int meta) {
        return meta == 0 ? new TileEntityAppliedEnergisticsConsumer()
                         : new TileEntityAppliedEnergisticsProducer();
    }
}
