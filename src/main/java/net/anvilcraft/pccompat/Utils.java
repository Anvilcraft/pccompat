package net.anvilcraft.pccompat;

import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapelessOreRecipeAdapter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class Utils {
    public static void registerStandardPowerConverterBlockIcons(
        IIconRegister reg, IIcon[] icons, String modPrefix
    ) {
        for (int i = 0; i < 4; i++) {
            String state = i % 2 == 0 ? "off" : "on";
            String type = i < 2 ? "consumer" : "producer";

            icons[i]
                = reg.registerIcon("pccompat:" + modPrefix + "_" + type + "_" + state);
        }
    }

    public static void registerConversionRecipes(Block block, int metaA, int metaB) {
        ItemStack stackA = new ItemStack(block, 1, metaA);
        ItemStack stackB = new ItemStack(block, 1, metaB);

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .ingredient(stackA)
            .output(stackB)
            .register();

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .ingredient(stackB)
            .output(stackA)
            .register();
    }
}
