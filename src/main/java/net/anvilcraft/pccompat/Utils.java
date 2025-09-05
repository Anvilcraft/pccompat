package net.anvilcraft.pccompat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import covers1624.powerconverters.tile.main.TileEntityEnergyBridge;
import net.anvilcraft.pccompat.recipe.RecipeBuilder;
import net.anvilcraft.pccompat.recipe.ShapelessOreRecipeAdapter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

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

    @SuppressWarnings("unchecked")
    public static Map<ForgeDirection, Double> getOutputRates(TileEntityEnergyBridge bridge) {
        try {
            Field rates = TileEntityEnergyBridge.class.getDeclaredField("_producerOutputRates");
            rates.setAccessible(true);
            Object value = rates.get(bridge);
            return (Map<ForgeDirection, Double>) value;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static ForgeDirection getSide(TileEntity reference, TileEntity target) {
        if (reference.xCoord > target.xCoord && reference.yCoord == target.yCoord && reference.zCoord == target.zCoord) {
            return ForgeDirection.WEST;
        } else if (reference.xCoord < target.xCoord && reference.yCoord == target.yCoord && reference.zCoord == target.zCoord) {
            return ForgeDirection.EAST;
        } else if (reference.xCoord == target.xCoord && reference.yCoord > target.yCoord && reference.zCoord == target.zCoord) {
            return ForgeDirection.DOWN;
        } else if (reference.xCoord == target.xCoord && reference.yCoord < target.yCoord && reference.zCoord == target.zCoord) {
            return ForgeDirection.UP;
        } else if (reference.xCoord == target.xCoord && reference.yCoord == target.yCoord && reference.zCoord > target.zCoord) {
            return ForgeDirection.NORTH;
        } else if (reference.xCoord == target.xCoord && reference.yCoord == target.yCoord && reference.zCoord < target.zCoord) {
            return ForgeDirection.SOUTH;
        } else {
            return ForgeDirection.UNKNOWN;
        }
    }
}
