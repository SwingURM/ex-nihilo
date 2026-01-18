package exnihilo.compatibility;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.ENBlocks;
import exnihilo.ENItems;
import exnihilo.ExNihilo;
import exnihilo.blocks.BlockBlackstoneGravel;
import exnihilo.blocks.BlockDeepslateGravel;
import exnihilo.items.ItemPebble;
import exnihilo.items.meshes.MeshType;
import exnihilo.registries.SieveRegistry;

/**
 * Compatibility module for Etfuturum mod.
 * Handles registration of 1.7.10+ only content:
 * - Gravel block sieve recipes (deepslate, blackstone, end stone)
 * - Various etfuturum items (iron_nugget, cactus, dead_bush)
 */
public class EtFuturum {

    public static void loadCompatibility() {
        registerEtFuturumBlocks();
        registerEtFuturumItems();
        registerEtFuturumPebbleRecipes();
        registerCrushedBlockRewards();
        registerEndStoneRewards();
        registerEtfuturumItemRewards();

        ExNihilo.log.info("--- Etfuturum Integration Complete!");
    }

    private static Item getItem(String domain, String name) {
        return (Item) Item.itemRegistry.getObject(domain + ":" + name);
    }

    private static void registerEtFuturumBlocks() {
        // Check if EtFuturum is loaded and has the required blocks
        if (!Loader.isModLoaded("etfuturum")) {
            return;
        }

        try {
            Class<?> modBlocksClass = Class.forName("ganymedes01.etfuturum.ModBlocks");

            // Deepslate exists in EtFuturum 2.5+ for 1.16+
            try {
                Object deepslateBlock = modBlocksClass.getField("DEEPSLATE")
                    .get(null);
                if (deepslateBlock != null) {
                    ENBlocks.DeepslateGravel = new BlockDeepslateGravel();
                    GameRegistry.registerBlock(ENBlocks.DeepslateGravel, ENBlocks.DeepslateGravel.getUnlocalizedName());
                    ExNihilo.log.info("Registered deepslate_gravel (EtFuturum detected)");
                }
            } catch (NoSuchFieldException e) {
                ExNihilo.log.info("EtFuturum detected but deepslate not available (old version)");
            }

            // Blackstone exists in EtFuturum 2.5+ for 1.16+
            try {
                Object blackstoneBlock = modBlocksClass.getField("BLACKSTONE")
                    .get(null);
                if (blackstoneBlock != null) {
                    ENBlocks.BlackstoneGravel = new BlockBlackstoneGravel();
                    GameRegistry
                        .registerBlock(ENBlocks.BlackstoneGravel, ENBlocks.BlackstoneGravel.getUnlocalizedName());
                    ExNihilo.log.info("Registered blackstone_gravel (EtFuturum detected)");
                }
            } catch (NoSuchFieldException e) {
                ExNihilo.log.info("EtFuturum detected but blackstone not available (old version)");
            }
        } catch (Exception e) {
            ExNihilo.log.warn("Failed to load EtFuturum integration for crushed stones: " + e.getMessage());
        }
    }

    private static void registerEtFuturumItems() {
        // Register new stone type pebbles (Andesite, Granite, Diorite, Deepslate,
        // Blackstone, Basalt, Tuff, Calcite)
        ENItems.PebbleAndesite = new ItemPebble("andesite");
        GameRegistry.registerItem(ENItems.PebbleAndesite, "pebble_andesite");
        ENItems.PebbleGranite = new ItemPebble("granite");
        GameRegistry.registerItem(ENItems.PebbleGranite, "pebble_granite");
        ENItems.PebbleDiorite = new ItemPebble("diorite");
        GameRegistry.registerItem(ENItems.PebbleDiorite, "pebble_diorite");
        ENItems.PebbleDeepslate = new ItemPebble("deepslate");
        GameRegistry.registerItem(ENItems.PebbleDeepslate, "pebble_deepslate");
        ENItems.PebbleBlackstone = new ItemPebble("blackstone");
        GameRegistry.registerItem(ENItems.PebbleBlackstone, "pebble_blackstone");
        ENItems.PebbleBasalt = new ItemPebble("basalt");
        GameRegistry.registerItem(ENItems.PebbleBasalt, "pebble_basalt");
        ENItems.PebbleTuff = new ItemPebble("tuff");
        GameRegistry.registerItem(ENItems.PebbleTuff, "pebble_tuff");
        ENItems.PebbleCalcite = new ItemPebble("calcite");
        GameRegistry.registerItem(ENItems.PebbleCalcite, "pebble_calcite");
    }

    private static void registerEtFuturumPebbleRecipes() {
        // Pebble -> stone variant recipes (2x2)
        // Andesite: etfuturum:stone@5, Granite: etfuturum:stone@1, Diorite:
        // etfuturum:stone@3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "stone"), 1, 5),
                "xx",
                "xx",
                'x',
                ENItems.PebbleAndesite));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "stone"), 1, 1),
                "xx",
                "xx",
                'x',
                ENItems.PebbleGranite));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "stone"), 1, 3),
                "xx",
                "xx",
                'x',
                ENItems.PebbleDiorite));

        // Deepslate pebble -> cobbled deepslate
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "cobbled_deepslate"), 1, 0),
                "xx",
                "xx",
                'x',
                ENItems.PebbleDeepslate));

        // Tuff, Calcite, Blackstone, Basalt
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "tuff"), 1, 0),
                "xx",
                "xx",
                'x',
                ENItems.PebbleTuff));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "calcite"), 1, 0),
                "xx",
                "xx",
                'x',
                ENItems.PebbleCalcite));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "blackstone"), 1, 0),
                "xx",
                "xx",
                'x',
                ENItems.PebbleBlackstone));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(GameRegistry.findItem("etfuturum", "basalt"), 1, 0),
                "xx",
                "xx",
                'x',
                ENItems.PebbleBasalt));
    }

    private static void registerCrushedBlockRewards() {
        // ============ BLACKSTONE GRAVEL ============
        // SILK mesh
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBasalt, 0, 3, 0.50f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBlackstone, 0, 4, 0.60f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gold_nugget, 0, 4, 0.20f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gunpowder, 0, 1, 0.07f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.magma_cream, 0, 1, 0.08f, MeshType.SILK);

        // FLINT mesh
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBasalt, 0, 3, 0.55f, MeshType.FLINT);
        SieveRegistry
            .registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBlackstone, 0, 4, 0.65f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gold_nugget, 0, 4, 0.23f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gunpowder, 0, 1, 0.09f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.magma_cream, 0, 1, 0.09f, MeshType.FLINT);

        // IRON mesh
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBasalt, 0, 4, 0.55f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBlackstone, 0, 5, 0.65f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gold_nugget, 0, 4, 0.25f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gunpowder, 0, 1, 0.09f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.magma_cream, 0, 1, 0.09f, MeshType.IRON);

        // GOLDEN mesh
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBasalt, 0, 4, 0.50f, MeshType.GOLDEN);
        SieveRegistry
            .registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBlackstone, 0, 5, 0.70f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gold_nugget, 0, 8, 0.33f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gunpowder, 0, 1, 0.10f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.magma_cream, 0, 1, 0.10f, MeshType.GOLDEN);

        // DIAMOND mesh
        SieveRegistry
            .registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBlackstone, 0, 5, 0.70f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gold_nugget, 0, 4, 0.28f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gunpowder, 0, 1, 0.11f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.magma_cream, 0, 1, 0.11f, MeshType.DIAMOND);

        // NETHERITE mesh
        SieveRegistry
            .registerBinomial(ENBlocks.BlackstoneGravel, ENItems.PebbleBlackstone, 0, 5, 0.75f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gold_nugget, 0, 4, 0.33f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.gunpowder, 0, 1, 0.11f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.BlackstoneGravel, Items.magma_cream, 0, 1, 0.12f, MeshType.NETHERITE);

        // ============ DEEPSLATE GRAVEL ============

        // SILK mesh
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleDeepslate, 0, 4, 0.50f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.diamond, 0, 1, 0.04f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.emerald, 0, 1, 0.03f, MeshType.SILK);

        // FLINT mesh
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleBasalt, 0, 4, 0.40f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleCalcite, 0, 4, 0.40f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleDeepslate, 0, 4, 0.50f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleTuff, 0, 4, 0.40f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.diamond, 0, 1, 0.05f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.emerald, 0, 1, 0.04f, MeshType.FLINT);

        // IRON mesh
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleDeepslate, 0, 4, 0.60f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.diamond, 0, 1, 0.06f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.emerald, 0, 1, 0.05f, MeshType.IRON);

        // GOLDEN mesh
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleDeepslate, 0, 4, 0.65f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.diamond, 0, 1, 0.08f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.emerald, 0, 1, 0.07f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.gold_nugget, 0, 3, 0.10f, MeshType.GOLDEN);

        // DIAMOND mesh
        SieveRegistry
            .registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleDeepslate, 0, 4, 0.65f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.diamond, 0, 1, 0.08f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.emerald, 0, 1, 0.08f, MeshType.DIAMOND);

        // NETHERITE mesh
        SieveRegistry
            .registerBinomial(ENBlocks.DeepslateGravel, ENItems.PebbleDeepslate, 0, 4, 0.70f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.diamond, 0, 1, 0.10f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.DeepslateGravel, Items.emerald, 0, 1, 0.10f, MeshType.NETHERITE);
    }

    private static void registerEndStoneRewards() {
        // ============ ENDER GRAVEL (for crushed end stone) ============

        Item chorusFruit = getItem("etfuturum", "chorus_fruit");
        Item chorusFlower = getItem("etfuturum", "chorus_flower");
        Item echoShard = getItem("etfuturum", "echo_shard");
        Item sculkShrieker = getItem("etfuturum", "sculk_shrieker");

        // SILK mesh
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_eye, 0, 1, 0.02f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_pearl, 0, 1, 0.07f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFruit, 0, 1, 0.09f, MeshType.SILK);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFlower, 0, 1, 0.04f, MeshType.SILK);

        // FLINT mesh
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_eye, 0, 1, 0.03f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_pearl, 0, 1, 0.08f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFruit, 0, 1, 0.11f, MeshType.FLINT);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFlower, 0, 1, 0.06f, MeshType.FLINT);

        // IRON mesh
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_eye, 0, 1, 0.04f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_pearl, 0, 1, 0.10f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFruit, 0, 1, 0.13f, MeshType.IRON);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFlower, 0, 1, 0.07f, MeshType.IRON);

        // GOLDEN mesh
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_eye, 0, 1, 0.07f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_pearl, 0, 1, 0.12f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFruit, 0, 1, 0.12f, MeshType.GOLDEN);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFlower, 0, 1, 0.06f, MeshType.GOLDEN);

        // DIAMOND mesh
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_eye, 0, 1, 0.09f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_pearl, 0, 1, 0.15f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFruit, 0, 1, 0.10f, MeshType.DIAMOND);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFlower, 0, 1, 0.04f, MeshType.DIAMOND);

        // NETHERITE mesh
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_eye, 0, 1, 0.09f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, Items.ender_pearl, 0, 1, 0.17f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFruit, 0, 1, 0.10f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, chorusFlower, 0, 1, 0.04f, MeshType.NETHERITE);
        SieveRegistry.registerBinomial(ENBlocks.EnderGravel, echoShard, 0, 1, 0.03f, MeshType.NETHERITE);
    }

    private static void registerEtfuturumItemRewards() {
        // Register etfuturum items from various blocks
        // Note: cactus is available in vanilla 1.7.10, no need to check EtFuturum

        Item ironNugget = getItem("etfuturum", "iron_nugget");
        Item deadBush = getItem("etfuturum", "dead_bush");

        SieveRegistry.registerBinomial(Blocks.dirt, ironNugget, 0, 1, 0.05f, MeshType.IRON);

        SieveRegistry.registerBinomial(ENBlocks.Dust, ironNugget, 0, 1, 0.06f, MeshType.IRON);

        SieveRegistry.registerBinomial(Blocks.dirt, ironNugget, 0, 1, 0.05f, MeshType.GOLDEN);

        SieveRegistry.registerBinomial(ENBlocks.Dust, ironNugget, 0, 1, 0.08f, MeshType.NETHERITE);

        // SAND mesh rewards - cactus is vanilla block in 1.7.10
        SieveRegistry.registerBinomial(Blocks.sand, new ItemStack(Blocks.cactus).getItem(), 0, 1, 0.13f, MeshType.SILK);
        SieveRegistry.registerBinomial(Blocks.sand, new ItemStack(Blocks.cactus).getItem(), 0, 1, 0.13f, MeshType.IRON);
        SieveRegistry
            .registerBinomial(Blocks.sand, new ItemStack(Blocks.cactus).getItem(), 0, 1, 0.10f, MeshType.GOLDEN);
        SieveRegistry
            .registerBinomial(Blocks.sand, new ItemStack(Blocks.cactus).getItem(), 0, 1, 0.15f, MeshType.NETHERITE);

        if (deadBush != null) {
            SieveRegistry.registerBinomial(Blocks.sand, deadBush, 0, 1, 0.08f, MeshType.SILK);
            SieveRegistry.registerBinomial(Blocks.sand, deadBush, 0, 1, 0.03f, MeshType.FLINT);
            SieveRegistry.registerBinomial(Blocks.sand, deadBush, 0, 1, 0.08f, MeshType.IRON);
            SieveRegistry.registerBinomial(Blocks.sand, deadBush, 0, 1, 0.06f, MeshType.GOLDEN);
        }
    }
}
