package exnihilo.registries;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import exnihilo.ENBlocks;
import exnihilo.ENItems;
import exnihilo.Fluids;
import exnihilo.data.ModData;
import exnihilo.registries.helpers.EntityWithItem;
import exnihilo.registries.helpers.FluidItemCombo;
import exnihilo.utils.ItemInfo;

public class BarrelRecipeRegistry {

    private static final HashMap<FluidItemCombo, ItemInfo> recipes = new HashMap<>();

    private static final HashSet<ItemInfo> renderOverride = new HashSet<>();

    private static final HashMap<FluidItemCombo, EntityWithItem> mobRecipes = new HashMap<>();

    private static final HashMap<FluidItemCombo, Fluid> fluidTransformRecipes = new HashMap<>();

    public static void addFluidItemRecipe(Fluid fluid, ItemStack inputStack, ItemStack outputStack) {
        addFluidItemRecipe(fluid, inputStack, outputStack, false);
    }

    public static void addFluidItemRecipe(Fluid fluid, ItemStack inputStack, ItemStack outputStack, boolean override) {
        recipes.put(new FluidItemCombo(fluid, inputStack), new ItemInfo(outputStack));
        if (override) renderOverride.add(new ItemInfo(outputStack));
    }

    public static void removeFluidItemRecipe(Fluid fluid, ItemStack inputStack, ItemStack outputStack) {
        recipes.remove(new ItemInfo(outputStack));
        renderOverride.remove(new ItemInfo(outputStack));
    }

    public static ItemInfo getOutput(FluidStack fluid_, ItemStack inputStack) {
        Fluid fluid;
        if (fluid_ == null) {
            fluid = null;
        } else {
            fluid = fluid_.getFluid();
        }
        return recipes.get(new FluidItemCombo(fluid, inputStack));
    }

    public static boolean getShouldRenderOverride(ItemStack stack) {
        return renderOverride.contains(new ItemInfo(stack));
    }

    public static void addMobRecipe(Fluid fluid, ItemStack inputStack, Class entity, String particleName,
        ItemStack peacefulDrop) {
        mobRecipes.put(new FluidItemCombo(fluid, inputStack), new EntityWithItem(entity, peacefulDrop, particleName));
    }

    public static void removeMobRecipe(Fluid fluid, ItemStack inputStack) {
        mobRecipes.remove(new FluidItemCombo(fluid, inputStack));
    }

    public static EntityWithItem getMobOutput(FluidStack fluid_, ItemStack inputStack) {
        Fluid fluid;
        if (fluid_ == null) {
            fluid = null;
        } else {
            fluid = fluid_.getFluid();
        }
        return mobRecipes.get(new FluidItemCombo(fluid, inputStack));
    }

    public static void addFluidTransformRecipe(Fluid inputFluid, Block inputBlock, int inputMeta, Fluid outputFluid) {
        fluidTransformRecipes.put(new FluidItemCombo(inputFluid, new ItemStack(inputBlock, 1, inputMeta)), outputFluid);
    }

    public static Fluid getFluidTransformRecipeOutput(Fluid inputFluid, Block inputBlock, int inputMeta) {
        return fluidTransformRecipes.get(new FluidItemCombo(inputFluid, new ItemStack(inputBlock, 1, inputMeta)));
    }

    public static void registerBaseRecipes() {
        if (ModData.ALLOW_BARREL_RECIPE_CLAY)
            addFluidItemRecipe(FluidRegistry.WATER, new ItemStack(ENBlocks.Dust), new ItemStack(Blocks.clay));
        if (ModData.ALLOW_BARREL_RECIPE_NETHERRACK)
            addFluidItemRecipe(FluidRegistry.LAVA, new ItemStack(Items.redstone), new ItemStack(Blocks.netherrack));
        if (ModData.ALLOW_BARREL_RECIPE_ENDSTONE) addFluidItemRecipe(
            FluidRegistry.LAVA,
            new ItemStack(Items.glowstone_dust),
            new ItemStack(Blocks.end_stone));
        if (ModData.ALLOW_BARREL_RECIPE_SOULSAND)
            addFluidItemRecipe(Fluids.fluidWitchWater, new ItemStack(Blocks.sand), new ItemStack(Blocks.soul_sand));
        if (ModData.ALLOW_BARREL_RECIPE_DARK_OAK) addFluidItemRecipe(
            Fluids.fluidWitchWater,
            new ItemStack(Blocks.sapling),
            new ItemStack(Blocks.sapling, 1, 5),
            true);
        if (ModData.ALLOW_BARREL_RECIPE_DOUBLE_FLOWERS) {
            addFluidItemRecipe(
                Fluids.fluidWitchWater,
                new ItemStack(Blocks.yellow_flower),
                new ItemStack(Blocks.double_plant),
                true);
            addFluidItemRecipe(
                Fluids.fluidWitchWater,
                new ItemStack(Blocks.red_flower, 1, 2),
                new ItemStack(Blocks.double_plant, 1, 1),
                true);
            addFluidItemRecipe(
                Fluids.fluidWitchWater,
                new ItemStack(Blocks.tallgrass, 1, 1),
                new ItemStack(Blocks.double_plant, 1, 2),
                true);
            addFluidItemRecipe(
                Fluids.fluidWitchWater,
                new ItemStack(Blocks.tallgrass, 1, 2),
                new ItemStack(Blocks.double_plant, 1, 3),
                true);
            addFluidItemRecipe(
                Fluids.fluidWitchWater,
                new ItemStack(Blocks.red_flower),
                new ItemStack(Blocks.double_plant, 1, 4),
                true);
            addFluidItemRecipe(
                Fluids.fluidWitchWater,
                new ItemStack(Blocks.red_flower, 1, 3),
                new ItemStack(Blocks.double_plant, 1, 5),
                true);
        }
        addFluidTransformRecipe(FluidRegistry.WATER, Blocks.mycelium, 0, Fluids.fluidWitchWater);
        Fluid seedOil = FluidRegistry.getFluid("seedoil");
        if (seedOil != null)
            addFluidItemRecipe(seedOil, new ItemStack(ENBlocks.BeeTrap), new ItemStack(ENBlocks.BeeTrapTreated));
        if (ModData.ALLOW_BARREL_RECIPE_BLAZE_RODS) addMobRecipe(
            FluidRegistry.LAVA,
            new ItemStack(ENItems.DollAngry),
            EntityBlaze.class,
            "lava",
            new ItemStack(Items.blaze_rod));
        if (ModData.ALLOW_BARREL_RECIPE_ENDER_PEARLS) addMobRecipe(
            Fluids.fluidWitchWater,
            new ItemStack(ENItems.DollCreepy),
            EntityEnderman.class,
            "portal",
            new ItemStack(Items.ender_pearl));
    }
}
