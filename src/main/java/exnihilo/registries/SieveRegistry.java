package exnihilo.registries;

import exnihilo.ENBlocks;
import exnihilo.ENItems;
import exnihilo.items.meshes.MeshType;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class SieveRegistry {

    public static HashMap<MeshType, HashMap<ItemInfo, ArrayList<SiftingResult>>> getSiftables() {
        return siftables;
    }

    private static final HashMap<MeshType, HashMap<ItemInfo, ArrayList<SiftingResult>>> siftables = new HashMap<>();

    static {
        for (MeshType meshType : MeshType.getValues()) {
            if (meshType == MeshType.NONE)
                continue;
            siftables.put(meshType, new HashMap<>());
        }
    }

    public static void register(Block source, int sourceMeta, Item output, int outputMeta, int rarity,
            MeshType meshType) {
        if (meshType == MeshType.NONE)
            return;
        if (source == null || output == null)
            return;
        if (rarity > 0) {
            ItemInfo iteminfo = new ItemInfo(source, sourceMeta);
            ArrayList<SiftingResult> res = siftables.get(meshType).get(iteminfo);
            if (res == null)
                res = new ArrayList<>();
            res.add(new SiftingResult(new ItemInfo(output, outputMeta), rarity));
            siftables.get(meshType).put(iteminfo, res);
        }
    }

    public static void register(Block source, int sourceMeta, Item output, int outputMeta, int rarity) {
        for (MeshType meshType : MeshType.getValues()) {
            register(source, sourceMeta, output, outputMeta, rarity, meshType);
        }
    }

    public static void register(Block source, Item output, int outputMeta, int rarity, MeshType meshType) {
        register(source, 0, output, outputMeta, rarity, meshType);
    }

    public static void register(Block source, Item output, int outputMeta, int rarity) {
        for (MeshType meshType : MeshType.getValues()) {
            register(source, output, outputMeta, rarity, meshType);
        }
    }

    // Binomial distribution registration: n = max drops, p = probability per
    // attempt
    public static void registerBinomial(Block source, int sourceMeta, Item output, int outputMeta,
            int n, float p, MeshType meshType) {
        if (meshType == MeshType.NONE)
            return;
        if (source == null || output == null)
            return;
        ItemInfo iteminfo = new ItemInfo(source, sourceMeta);
        ArrayList<SiftingResult> res = siftables.get(meshType).get(iteminfo);
        if (res == null)
            res = new ArrayList<>();
        res.add(SiftingResult.binomial(new ItemInfo(output, outputMeta), n, p));
        siftables.get(meshType).put(iteminfo, res);
    }

    // Convenience overload without sourceMeta
    public static void registerBinomial(Block source, Item output, int outputMeta,
            int n, float p, MeshType meshType) {
        registerBinomial(source, 0, output, outputMeta, n, p, meshType);
    }

    public static ArrayList<SiftingResult> getSiftingOutput(Block block, int meta, MeshType meshType) {
        return siftables.get(meshType).get(new ItemInfo(block, meta));
    }

    public static ArrayList<SiftingResult> getSiftingOutput(ItemInfo info, MeshType meshType) {
        return siftables.get(meshType).get(info);
    }

    public static boolean registered(Block block, int meta, MeshType meshType) {
        HashMap<ItemInfo, ArrayList<SiftingResult>> res = siftables.get(meshType);
        if (res == null)
            return false;
        return res.containsKey(new ItemInfo(block, meta));
    }

    public static boolean registered(Block block, MeshType meshType) {
        HashMap<ItemInfo, ArrayList<SiftingResult>> res = siftables.get(meshType);
        if (res == null)
            return false;
        return res.containsKey(new ItemInfo(block, 32767));
    }

    public static void unregisterReward(Block block, int meta, Item output, int outputMeta, MeshType meshType) {
        ItemInfo iteminfo = new ItemInfo(block, meta);
        ArrayList<SiftingResult> res = siftables.get(meshType).get(iteminfo);
        if (res == null)
            return;
        res.removeIf(sr -> sr.drop.getItem() == output && sr.drop.getMeta() == outputMeta);
        if (res.isEmpty())
            siftables.get(meshType).remove(iteminfo);
    }

    public static void unregisterReward(Block block, int meta, Item output, int outputMeta) {
        for (MeshType meshType : MeshType.getValues()) {
            if (meshType == MeshType.NONE)
                continue;
            unregisterReward(block, meta, output, outputMeta, meshType);
        }
    }

    public static void unregisterRewardFromAllBlocks(Item output, int outputMeta) {
        for (MeshType meshType : MeshType.getValues()) {
            if (meshType == MeshType.NONE)
                continue;
            for (ItemInfo iteminfo : siftables.get(meshType).keySet())
                unregisterReward(Block.getBlockFromItem(iteminfo.getItem()), iteminfo.getMeta(), output, outputMeta,
                        meshType);
        }
    }

    public static void unregisterAllRewardsFromBlock(Block block, int meta) {
        for (MeshType meshType : MeshType.getValues()) {
            if (meshType == MeshType.NONE)
                continue;
            siftables.get(meshType).remove(new ItemInfo(block, meta));
        }
    }

    public static void registerRewards() {

        // ============ DIRT ============

        // SILK mesh
        registerBinomial(Blocks.dirt, ENItems.GrassSeeds, 0, 1, 0.10f, MeshType.SILK);
        registerBinomial(Blocks.dirt, ENItems.Spores, 0, 1, 0.03f, MeshType.SILK);
        registerBinomial(Blocks.dirt, Items.flint, 0, 1, 0.25f, MeshType.SILK);
        registerBinomial(Blocks.dirt, Items.melon_seeds, 0, 1, 0.10f, MeshType.SILK);
        registerBinomial(Blocks.dirt, Items.pumpkin_seeds, 0, 1, 0.10f, MeshType.SILK);
        registerBinomial(Blocks.dirt, Items.wheat_seeds, 0, 1, 0.12f, MeshType.SILK);

        // FLINT mesh
        registerBinomial(Blocks.dirt, ENItems.GrassSeeds, 0, 1, 0.15f, MeshType.FLINT);
        registerBinomial(Blocks.dirt, ENItems.Spores, 0, 1, 0.05f, MeshType.FLINT);
        registerBinomial(Blocks.dirt, Items.flint, 0, 1, 0.30f, MeshType.FLINT);
        registerBinomial(Blocks.dirt, Items.melon_seeds, 0, 1, 0.12f, MeshType.FLINT);
        registerBinomial(Blocks.dirt, Items.pumpkin_seeds, 0, 1, 0.12f, MeshType.FLINT);
        registerBinomial(Blocks.dirt, Items.wheat_seeds, 0, 1, 0.15f, MeshType.FLINT);

        // IRON mesh
        registerBinomial(Blocks.dirt, ENItems.GrassSeeds, 0, 1, 0.17f, MeshType.IRON);
        registerBinomial(Blocks.dirt, ENItems.Spores, 0, 1, 0.10f, MeshType.IRON);
        registerBinomial(Blocks.dirt, Items.flint, 0, 1, 0.30f, MeshType.IRON);
        registerBinomial(Blocks.dirt, Items.melon_seeds, 0, 1, 0.15f, MeshType.IRON);
        registerBinomial(Blocks.dirt, Items.pumpkin_seeds, 0, 1, 0.15f, MeshType.IRON);
        registerBinomial(Blocks.dirt, Items.wheat_seeds, 0, 1, 0.17f, MeshType.IRON);

        // GOLDEN mesh
        registerBinomial(Blocks.dirt, ENItems.GrassSeeds, 0, 1, 0.25f, MeshType.GOLDEN);
        registerBinomial(Blocks.dirt, ENItems.Spores, 0, 1, 0.13f, MeshType.GOLDEN);
        registerBinomial(Blocks.dirt, Items.flint, 0, 1, 0.20f, MeshType.GOLDEN);
        registerBinomial(Blocks.dirt, Items.melon_seeds, 0, 1, 0.17f, MeshType.GOLDEN);
        registerBinomial(Blocks.dirt, Items.pumpkin_seeds, 0, 1, 0.17f, MeshType.GOLDEN);
        registerBinomial(Blocks.dirt, Items.wheat_seeds, 0, 1, 0.20f, MeshType.GOLDEN);

        // DIAMOND mesh
        registerBinomial(Blocks.dirt, ENItems.GrassSeeds, 0, 1, 0.15f, MeshType.DIAMOND);
        registerBinomial(Blocks.dirt, ENItems.Spores, 0, 1, 0.10f, MeshType.DIAMOND);
        registerBinomial(Blocks.dirt, Items.flint, 0, 3, 0.30f, MeshType.DIAMOND);

        // NETHERITE mesh
        registerBinomial(Blocks.dirt, ENItems.GrassSeeds, 0, 1, 0.20f, MeshType.NETHERITE);
        registerBinomial(Blocks.dirt, ENItems.Spores, 0, 1, 0.20f, MeshType.NETHERITE);
        registerBinomial(Blocks.dirt, Items.flint, 0, 3, 0.40f, MeshType.NETHERITE);

        // ============ DUST ============

        // SILK mesh
        registerBinomial(ENBlocks.Dust, Items.blaze_powder, 0, 1, 0.03f, MeshType.SILK);
        registerBinomial(ENBlocks.Dust, Items.glowstone_dust, 0, 1, 0.04f, MeshType.SILK);
        registerBinomial(ENBlocks.Dust, Items.gunpowder, 0, 1, 0.10f, MeshType.SILK);
        registerBinomial(ENBlocks.Dust, Items.redstone, 0, 1, 0.06f, MeshType.SILK);

        // FLINT mesh
        registerBinomial(ENBlocks.Dust, Items.blaze_powder, 0, 1, 0.04f, MeshType.FLINT);
        registerBinomial(ENBlocks.Dust, Items.glowstone_dust, 0, 1, 0.07f, MeshType.FLINT);
        registerBinomial(ENBlocks.Dust, Items.gunpowder, 0, 1, 0.11f, MeshType.FLINT);
        registerBinomial(ENBlocks.Dust, Items.redstone, 0, 1, 0.09f, MeshType.FLINT);

        // IRON mesh
        registerBinomial(ENBlocks.Dust, Items.blaze_powder, 0, 1, 0.05f, MeshType.IRON);
        registerBinomial(ENBlocks.Dust, Items.glowstone_dust, 0, 1, 0.09f, MeshType.IRON);
        registerBinomial(ENBlocks.Dust, Items.gunpowder, 0, 1, 0.13f, MeshType.IRON);
        registerBinomial(ENBlocks.Dust, Items.redstone, 0, 1, 0.10f, MeshType.IRON);

        // GOLDEN mesh
        registerBinomial(ENBlocks.Dust, Items.blaze_powder, 0, 1, 0.06f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.Dust, Items.glowstone_dust, 0, 1, 0.11f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.Dust, Items.gold_nugget, 0, 2, 0.18f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.Dust, Items.gunpowder, 0, 1, 0.13f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.Dust, Items.redstone, 0, 1, 0.12f, MeshType.GOLDEN);

        // DIAMOND mesh
        registerBinomial(ENBlocks.Dust, Items.blaze_powder, 0, 1, 0.06f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.Dust, Items.glowstone_dust, 0, 1, 0.11f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.Dust, Items.gold_nugget, 0, 1, 0.08f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.Dust, Items.gunpowder, 0, 1, 0.14f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.Dust, Items.redstone, 0, 1, 0.12f, MeshType.DIAMOND);

        // NETHERITE mesh
        registerBinomial(ENBlocks.Dust, Items.blaze_powder, 0, 1, 0.10f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.Dust, Items.glowstone_dust, 0, 1, 0.15f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.Dust, Items.gold_nugget, 0, 1, 0.08f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.Dust, Items.gunpowder, 0, 1, 0.14f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.Dust, Items.redstone, 0, 1, 0.14f, MeshType.NETHERITE);

        // ============ GRAVEL ============

        // SILK mesh
        registerBinomial(Blocks.gravel, Items.coal, 0, 1, 0.10f, MeshType.SILK);
        registerBinomial(Blocks.gravel, Items.diamond, 0, 1, 0.02f, MeshType.SILK);
        registerBinomial(Blocks.gravel, Items.emerald, 0, 1, 0.01f, MeshType.SILK);
        registerBinomial(Blocks.gravel, Items.flint, 0, 1, 0.20f, MeshType.SILK);

        // FLINT mesh
        registerBinomial(Blocks.gravel, Items.coal, 0, 1, 0.12f, MeshType.FLINT);
        registerBinomial(Blocks.gravel, Items.diamond, 0, 1, 0.03f, MeshType.FLINT);
        registerBinomial(Blocks.gravel, Items.emerald, 0, 1, 0.01f, MeshType.FLINT);
        registerBinomial(Blocks.gravel, Items.flint, 0, 1, 0.25f, MeshType.FLINT);

        // IRON mesh
        registerBinomial(Blocks.gravel, Items.coal, 0, 1, 0.15f, MeshType.IRON);
        registerBinomial(Blocks.gravel, Items.diamond, 0, 1, 0.05f, MeshType.IRON);
        registerBinomial(Blocks.gravel, Items.emerald, 0, 1, 0.04f, MeshType.IRON);
        registerBinomial(Blocks.gravel, Items.flint, 0, 1, 0.15f, MeshType.IRON);

        // GOLDEN mesh
        registerBinomial(Blocks.gravel, Items.coal, 0, 1, 0.20f, MeshType.GOLDEN);
        registerBinomial(Blocks.gravel, Items.diamond, 0, 1, 0.09f, MeshType.GOLDEN);
        registerBinomial(Blocks.gravel, Items.emerald, 0, 1, 0.09f, MeshType.GOLDEN);
        registerBinomial(Blocks.gravel, Items.flint, 0, 1, 0.13f, MeshType.GOLDEN);
        registerBinomial(Blocks.gravel, Items.gold_nugget, 0, 1, 0.08f, MeshType.GOLDEN);

        // DIAMOND mesh
        registerBinomial(Blocks.gravel, Items.coal, 0, 1, 0.06f, MeshType.DIAMOND);
        registerBinomial(Blocks.gravel, Items.diamond, 0, 1, 0.08f, MeshType.DIAMOND);
        registerBinomial(Blocks.gravel, Items.emerald, 0, 1, 0.07f, MeshType.DIAMOND);
        registerBinomial(Blocks.gravel, Items.flint, 0, 1, 0.05f, MeshType.DIAMOND);

        // NETHERITE mesh
        registerBinomial(Blocks.gravel, Items.coal, 0, 1, 0.06f, MeshType.NETHERITE);
        registerBinomial(Blocks.gravel, Items.diamond, 0, 1, 0.10f, MeshType.NETHERITE);
        registerBinomial(Blocks.gravel, Items.emerald, 0, 1, 0.09f, MeshType.NETHERITE);
        registerBinomial(Blocks.gravel, Items.gold_nugget, 0, 1, 0.04f, MeshType.NETHERITE);

        // ============ SAND ============

        // SILK mesh
        registerBinomial(Blocks.sand, Items.flint, 0, 1, 0.20f, MeshType.SILK);
        registerBinomial(Blocks.sand, Items.gold_nugget, 0, 1, 0.13f, MeshType.SILK);

        // FLINT mesh
        registerBinomial(Blocks.sand, Items.flint, 0, 2, 0.20f, MeshType.FLINT);
        registerBinomial(Blocks.sand, Items.gold_nugget, 0, 1, 0.16f, MeshType.FLINT);

        // IRON mesh
        registerBinomial(Blocks.sand, Items.flint, 0, 1, 0.23f, MeshType.IRON);
        registerBinomial(Blocks.sand, Items.gold_nugget, 0, 1, 0.18f, MeshType.IRON);

        // GOLDEN mesh
        registerBinomial(Blocks.sand, Items.flint, 0, 1, 0.18f, MeshType.GOLDEN);
        registerBinomial(Blocks.sand, Items.gold_nugget, 0, 3, 0.28f, MeshType.GOLDEN);

        // DIAMOND mesh
        registerBinomial(Blocks.sand, Items.flint, 0, 1, 0.23f, MeshType.DIAMOND);
        registerBinomial(Blocks.sand, Items.gold_nugget, 0, 1, 0.22f, MeshType.DIAMOND);

        // NETHERITE mesh
        registerBinomial(Blocks.sand, Items.flint, 0, 2, 0.23f, MeshType.NETHERITE);
        registerBinomial(Blocks.sand, Items.gold_nugget, 0, 1, 0.23f, MeshType.NETHERITE);

        // ============ SOUL_SAND ============

        // SILK mesh
        registerBinomial(Blocks.soul_sand, Items.bone, 0, 1, 0.08f, MeshType.SILK);
        registerBinomial(Blocks.soul_sand, Items.ghast_tear, 0, 1, 0.06f, MeshType.SILK);
        registerBinomial(Blocks.soul_sand, Items.glowstone_dust, 0, 1, 0.06f, MeshType.SILK);
        registerBinomial(Blocks.soul_sand, Items.gunpowder, 0, 1, 0.07f, MeshType.SILK);
        registerBinomial(Blocks.soul_sand, Items.nether_wart, 0, 1, 0.06f, MeshType.SILK);
        registerBinomial(Blocks.soul_sand, Items.quartz, 0, 1, 0.12f, MeshType.SILK);

        // FLINT mesh
        registerBinomial(Blocks.soul_sand, Items.bone, 0, 1, 0.10f, MeshType.FLINT);
        registerBinomial(Blocks.soul_sand, Items.ghast_tear, 0, 1, 0.07f, MeshType.FLINT);
        registerBinomial(Blocks.soul_sand, Items.glowstone_dust, 0, 1, 0.07f, MeshType.FLINT);
        registerBinomial(Blocks.soul_sand, Items.gunpowder, 0, 1, 0.08f, MeshType.FLINT);
        registerBinomial(Blocks.soul_sand, Items.nether_wart, 0, 1, 0.06f, MeshType.FLINT);
        registerBinomial(Blocks.soul_sand, Items.quartz, 0, 1, 0.14f, MeshType.FLINT);

        // IRON mesh
        registerBinomial(Blocks.soul_sand, Items.bone, 0, 1, 0.08f, MeshType.IRON);
        registerBinomial(Blocks.soul_sand, Items.ghast_tear, 0, 1, 0.06f, MeshType.IRON);
        registerBinomial(Blocks.soul_sand, Items.glowstone_dust, 0, 1, 0.06f, MeshType.IRON);
        registerBinomial(Blocks.soul_sand, Items.gunpowder, 0, 1, 0.07f, MeshType.IRON);
        registerBinomial(Blocks.soul_sand, Items.nether_wart, 0, 1, 0.05f, MeshType.IRON);
        registerBinomial(Blocks.soul_sand, Items.quartz, 0, 1, 0.15f, MeshType.IRON);

        // GOLDEN mesh
        registerBinomial(Blocks.soul_sand, Items.bone, 0, 1, 0.11f, MeshType.GOLDEN);
        registerBinomial(Blocks.soul_sand, Items.ghast_tear, 0, 1, 0.08f, MeshType.GOLDEN);
        registerBinomial(Blocks.soul_sand, Items.glowstone_dust, 0, 1, 0.09f, MeshType.GOLDEN);
        registerBinomial(Blocks.soul_sand, Items.gold_nugget, 0, 1, 0.15f, MeshType.GOLDEN);
        registerBinomial(Blocks.soul_sand, Items.gunpowder, 0, 1, 0.10f, MeshType.GOLDEN);
        registerBinomial(Blocks.soul_sand, Items.nether_wart, 0, 1, 0.08f, MeshType.GOLDEN);
        registerBinomial(Blocks.soul_sand, Items.quartz, 0, 1, 0.17f, MeshType.GOLDEN);

        // DIAMOND mesh
        registerBinomial(Blocks.soul_sand, Items.ghast_tear, 0, 1, 0.09f, MeshType.DIAMOND);
        registerBinomial(Blocks.soul_sand, Items.glowstone_dust, 0, 1, 0.11f, MeshType.DIAMOND);
        registerBinomial(Blocks.soul_sand, Items.gunpowder, 0, 1, 0.11f, MeshType.DIAMOND);
        registerBinomial(Blocks.soul_sand, Items.nether_wart, 0, 1, 0.10f, MeshType.DIAMOND);
        registerBinomial(Blocks.soul_sand, Items.quartz, 0, 1, 0.19f, MeshType.DIAMOND);

        // NETHERITE mesh
        registerBinomial(Blocks.soul_sand, Items.ghast_tear, 0, 1, 0.11f, MeshType.NETHERITE);
        registerBinomial(Blocks.soul_sand, Items.glowstone_dust, 0, 1, 0.13f, MeshType.NETHERITE);
        registerBinomial(Blocks.soul_sand, Items.gold_nugget, 0, 1, 0.15f, MeshType.NETHERITE);
        registerBinomial(Blocks.soul_sand, Items.gunpowder, 0, 1, 0.14f, MeshType.NETHERITE);
        registerBinomial(Blocks.soul_sand, Items.nether_wart, 0, 1, 0.12f, MeshType.NETHERITE);
        registerBinomial(Blocks.soul_sand, Items.quartz, 0, 1, 0.21f, MeshType.NETHERITE);

        // ============ CRUSHED_NETHERRACK ============

        // SILK mesh
        if (ENItems.PebbleBasalt != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBasalt, 0, 3, 0.30f, MeshType.SILK);
        }
        if (ENItems.PebbleBlackstone != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBlackstone, 0, 3, 0.40f, MeshType.SILK);
        }
        registerBinomial(ENBlocks.NetherGravel, Items.blaze_powder, 0, 1, 0.08f, MeshType.SILK);
        registerBinomial(ENBlocks.NetherGravel, Items.gold_nugget, 0, 1, 0.07f, MeshType.SILK);
        registerBinomial(ENBlocks.NetherGravel, Items.gunpowder, 0, 1, 0.08f, MeshType.SILK);
        registerBinomial(ENBlocks.NetherGravel, Items.magma_cream, 0, 1, 0.05f, MeshType.SILK);
        registerBinomial(ENBlocks.NetherGravel, Items.quartz, 0, 1, 0.08f, MeshType.SILK);

        // FLINT mesh
        if (ENItems.PebbleBasalt != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBasalt, 0, 4, 0.40f, MeshType.FLINT);
        }
        if (ENItems.PebbleBlackstone != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBlackstone, 0, 4, 0.50f, MeshType.FLINT);
        }
        registerBinomial(ENBlocks.NetherGravel, Items.blaze_powder, 0, 1, 0.09f, MeshType.FLINT);
        registerBinomial(ENBlocks.NetherGravel, Items.gold_nugget, 0, 1, 0.08f, MeshType.FLINT);
        registerBinomial(ENBlocks.NetherGravel, Items.gunpowder, 0, 1, 0.09f, MeshType.FLINT);
        registerBinomial(ENBlocks.NetherGravel, Items.magma_cream, 0, 1, 0.06f, MeshType.FLINT);
        registerBinomial(ENBlocks.NetherGravel, Items.quartz, 0, 1, 0.09f, MeshType.FLINT);

        // IRON mesh
        if (ENItems.PebbleBasalt != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBasalt, 0, 4, 0.45f, MeshType.IRON);
        }
        if (ENItems.PebbleBlackstone != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBlackstone, 0, 4, 0.60f, MeshType.IRON);
        }
        registerBinomial(ENBlocks.NetherGravel, Items.blaze_powder, 0, 1, 0.10f, MeshType.IRON);
        registerBinomial(ENBlocks.NetherGravel, Items.gold_nugget, 0, 1, 0.10f, MeshType.IRON);
        registerBinomial(ENBlocks.NetherGravel, Items.gunpowder, 0, 1, 0.10f, MeshType.IRON);
        registerBinomial(ENBlocks.NetherGravel, Items.magma_cream, 0, 1, 0.07f, MeshType.IRON);
        registerBinomial(ENBlocks.NetherGravel, Items.quartz, 0, 1, 0.11f, MeshType.IRON);

        // GOLDEN mesh
        if (ENItems.PebbleBasalt != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBasalt, 0, 4, 0.45f, MeshType.GOLDEN);
        }
        if (ENItems.PebbleBlackstone != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBlackstone, 0, 4, 0.60f, MeshType.GOLDEN);
        }
        registerBinomial(ENBlocks.NetherGravel, Items.blaze_powder, 0, 1, 0.11f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.NetherGravel, Items.gold_nugget, 0, 1, 0.14f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.NetherGravel, Items.gunpowder, 0, 1, 0.11f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.NetherGravel, Items.magma_cream, 0, 1, 0.08f, MeshType.GOLDEN);
        registerBinomial(ENBlocks.NetherGravel, Items.quartz, 0, 1, 0.13f, MeshType.GOLDEN);

        // DIAMOND mesh
        if (ENItems.PebbleBlackstone != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBlackstone, 0, 4, 0.60f, MeshType.DIAMOND);
        }
        registerBinomial(ENBlocks.NetherGravel, Items.blaze_powder, 0, 1, 0.14f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.NetherGravel, Items.gold_nugget, 0, 1, 0.12f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.NetherGravel, Items.gunpowder, 0, 1, 0.13f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.NetherGravel, Items.magma_cream, 0, 1, 0.10f, MeshType.DIAMOND);
        registerBinomial(ENBlocks.NetherGravel, Items.quartz, 0, 1, 0.13f, MeshType.DIAMOND);

        // NETHERITE mesh
        if (ENItems.PebbleBlackstone != null) {
            registerBinomial(ENBlocks.NetherGravel, ENItems.PebbleBlackstone, 0, 5, 0.65f, MeshType.NETHERITE);
        }
        registerBinomial(ENBlocks.NetherGravel, Items.blaze_powder, 0, 1, 0.15f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.NetherGravel, Items.gold_nugget, 0, 1, 0.12f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.NetherGravel, Items.gunpowder, 0, 1, 0.13f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.NetherGravel, Items.magma_cream, 0, 1, 0.10f, MeshType.NETHERITE);
        registerBinomial(ENBlocks.NetherGravel, Items.quartz, 0, 1, 0.15f, MeshType.NETHERITE);
    }

    public static HashMap<MeshType, ArrayList<ItemInfo>> getSources(ItemStack reward) {
        HashMap<MeshType, ArrayList<ItemInfo>> res = new HashMap<>();
        for (MeshType meshType : MeshType.getValues()) {
            if (meshType == MeshType.NONE)
                continue;
            res.put(meshType, new ArrayList<>());
            for (ItemInfo entry : siftables.get(meshType).keySet()) {
                for (SiftingResult sift : siftables.get(meshType).get(entry)) {
                    if ((new ItemInfo(sift.drop.getItem(), sift.drop.getMeta())).equals(new ItemInfo(reward)))
                        res.get(meshType).add(entry);
                }
            }
        }
        return res;
    }
}
