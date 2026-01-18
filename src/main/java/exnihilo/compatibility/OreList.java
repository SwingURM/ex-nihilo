package exnihilo.compatibility;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import exnihilo.registries.HammerRegistry;
import exnihilo.registries.OreRegistry;
import exnihilo.registries.helpers.Color;

public class OreList {

    public static boolean dropCopper = false;

    public static boolean dropTin = false;

    public static boolean dropSilver = false;

    public static boolean dropLead = false;

    public static boolean dropNickel = false;

    public static boolean dropPlatinum = false;

    public static boolean dropAluminum = false;

    public static boolean dropOsmium = false;

    public static void load(Configuration config) {
        String CATEGORY_ORE_OPTIONS = "force ore to generate";
        String CATEGORY_ORECHANCES = "Ore Drop Chances - Value is 1 in X that the drop will occur. Higher number = lower chance of drop.";
        dropCopper = config.get(CATEGORY_ORE_OPTIONS, "copper", false)
            .getBoolean(false);
        dropTin = config.get(CATEGORY_ORE_OPTIONS, "tin", false)
            .getBoolean(false);
        dropLead = config.get(CATEGORY_ORE_OPTIONS, "lead", false)
            .getBoolean(false);
        dropSilver = config.get(CATEGORY_ORE_OPTIONS, "silver", false)
            .getBoolean(false);
        dropNickel = config.get(CATEGORY_ORE_OPTIONS, "nickel", false)
            .getBoolean(false);
        dropPlatinum = config.get(CATEGORY_ORE_OPTIONS, "platinum", false)
            .getBoolean(false);
        dropAluminum = config.get(CATEGORY_ORE_OPTIONS, "aluminum", false)
            .getBoolean(false);
        dropOsmium = config.get(CATEGORY_ORE_OPTIONS, "osmium", false)
            .getBoolean(false);
        for (Type type : Type.values()) type.setChance(
            config.get(CATEGORY_ORECHANCES, type.name(), type.getChance())
                .getInt());
    }

    public static void processOreDict() {
        String[] oreString = { "iron", "gold", "copper", "tin", "silver", "lead", "nickel", "platinum", "aluminum",
            "osmium" };
        for (String name : oreString) {
            String name2 = name.substring(0, 1)
                .toUpperCase()
                + name.substring(1)
                    .toLowerCase();
            ArrayList<ItemStack> ores = OreDictionary.getOres("ore" + name2);
            if (ores.size() > 0) for (ItemStack i : ores) {
                System.out.println(
                    "Registering " + Item.itemRegistry.getNameForObject(i.getItem()) + ":" + i.getItemDamage());
                if (Block.getBlockFromItem(i.getItem()) != Blocks.air) {
                    HammerRegistry.registerOre(
                        Block.getBlockFromItem(i.getItem()),
                        i.getItemDamage(),
                        OreRegistry.brokenTable.get(name),
                        0);
                    continue;
                }
                System.out
                    .println(Item.itemRegistry.getNameForObject(i.getItem()) + ":" + i.getItemDamage() + " is null!");
            }
        }
    }

    public static void registerOres() {
        boolean ignoreOreDict = false;
        OreRegistry.createOverworldOre("iron", new Color("F2AB7C"), Type.Iron.getChance(), Items.iron_ingot);
        OreRegistry.createNetherOre("iron", new Color("F2AB7C"), Type.NetherIron.getChance(), Items.iron_ingot);
        OreRegistry.createOverworldOre("gold", new Color("FFD000"), Type.Gold.getChance(), Items.gold_ingot);
        OreRegistry.createNetherOre("gold", new Color("FFD000"), Type.NetherGold.getChance(), Items.gold_ingot);
        if (OreDictionary.getOres("oreCopper")
            .size() > 0 || ignoreOreDict
            || dropCopper) {
            OreRegistry.createOverworldOre("copper", new Color("FF4D00"), Type.Copper.getChance());
            OreRegistry.createNetherOre("copper", new Color("FF4D00"), Type.NetherCopper.getChance());
        }
        if (OreDictionary.getOres("oreTin")
            .size() > 0 || ignoreOreDict
            || dropTin) {
            OreRegistry.createOverworldOre("tin", new Color("ABC9B6"), Type.Tin.getChance());
            OreRegistry.createEnderOre("tin", new Color("ABC9B6"), Type.EnderTin.getChance());
        }
        if (OreDictionary.getOres("oreSilver")
            .size() > 0 || ignoreOreDict
            || dropSilver) {
            OreRegistry.createOverworldOre("silver", new Color("8CC9FF"), Type.Silver.getChance());
            OreRegistry.createEnderOre("silver", new Color("8CC9FF"), Type.EnderSilver.getChance());
        }
        if (OreDictionary.getOres("oreLead")
            .size() > 0 || ignoreOreDict
            || dropLead) {
            OreRegistry.createOverworldOre("lead", new Color("2D2563"), Type.Lead.getChance());
            OreRegistry.createEnderOre("lead", new Color("2D2563"), Type.EnderLead.getChance());
        }
        if (OreDictionary.getOres("oreNickel")
            .size() > 0 || ignoreOreDict
            || dropNickel) {
            OreRegistry.createOverworldOre("nickel", new Color("BAB877"), Type.Nickel.getChance());
            OreRegistry.createNetherOre("nickel", new Color("BAB877"), Type.NetherNickel.getChance());
        }
        if (OreDictionary.getOres("orePlatinum")
            .size() > 0 || ignoreOreDict
            || dropPlatinum) {
            OreRegistry.createOverworldOre("platinum", new Color("38CDFF"), Type.Platinum.getChance());
            OreRegistry.createEnderOre("platinum", new Color("38CDFF"), Type.EnderPlatinum.getChance());
        }
        if (OreDictionary.getOres("oreAluminum")
            .size() > 0
            || OreDictionary.getOres("oreAluminium")
                .size() > 0
            || ignoreOreDict
            || dropAluminum) {
            OreRegistry.createOverworldOre("aluminum", new Color("FFC7C7"), Type.Aluminum.getChance());
            Item ingot = OreRegistry.getIngot("aluminum");
            if (ingot != null) OreRegistry.registerOreDict("aluminium", ingot);
        }
        if (OreDictionary.getOres("oreOsmium")
            .size() > 0 || ignoreOreDict
            || dropOsmium) OreRegistry.createOverworldOre("osmium", new Color("608FC4"), Type.Osmium.getChance());
    }

    public enum Type {

        Iron(5, "F2AB7C"),
        NetherIron(6, "F2AB7C"),
        Gold(32, "FFD000"),
        NetherGold(6, "FFD000"),
        Tin(18, "ABC9B6"),
        EnderTin(10, "ABC9B6"),
        Copper(18, "FF4D00"),
        NetherCopper(10, "FF4D00"),
        Nickel(32, "BAB877"),
        NetherNickel(10, "BAB877"),
        Platinum(128, "38CDFF"),
        EnderPlatinum(20, "38CDFF"),
        Silver(45, "8CC9FF"),
        EnderSilver(6, "8CC9FF"),
        Lead(32, "2D2563"),
        EnderLead(6, "2D2563"),
        Aluminum(8, "FFC7C7"),
        Osmium(10, "608FC4");

        private int chance;

        private final String color;

        Type(int chance, String color) {
            this.chance = chance;
            this.color = color;
        }

        private int getChance() {
            return this.chance;
        }

        public void setChance(int newChance) {
            this.chance = newChance;
        }

        public String getColor() {
            return this.color;
        }
    }

    public enum OreDimension {
        OVERWORLD,
        NETHER,
        ENDER
    }
}
