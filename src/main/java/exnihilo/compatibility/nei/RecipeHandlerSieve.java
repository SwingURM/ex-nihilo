package exnihilo.compatibility.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo.items.meshes.MeshType;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;

public class RecipeHandlerSieve extends TemplateRecipeHandler {

    private static final DecimalFormat df = new DecimalFormat("0.0");

    public class CachedSieveRecipe extends TemplateRecipeHandler.CachedRecipe {

        private final List<PositionedStack> input = new ArrayList<>();
        private final List<PositionedStack> outputs = new ArrayList<>();
        private final Map<Integer, SiftingResult> resultToChance = new HashMap<>();

        public Point focus;

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(RecipeHandlerSieve.this.cycleticks / 20, this.input);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        public SiftingResult getChanceForResult(int index) {
            return resultToChance.get(index);
        }

        public CachedSieveRecipe(List<ItemStack> variations, MeshType meshType, ItemStack base, ItemStack focus,
            List<SiftingResult> chances) {
            super();
            PositionedStack pstack_item = new PositionedStack((base != null) ? base : variations, 11, 3);
            PositionedStack pstack_mesh = new PositionedStack(
                new ItemStack(MeshType.getItemForType(meshType), 1, 0),
                11,
                39);
            pstack_item.setMaxSize(1);
            pstack_mesh.setMaxSize(1);
            this.input.add(pstack_item);
            this.input.add(pstack_mesh);
            int row = 0;
            int col = 0;
            for (int i = 0; i < variations.size(); i++) {
                ItemStack v = variations.get(i);
                this.outputs.add(new PositionedStack(v, 39 + 18 * col, 3 + 18 * row));
                if (i < chances.size()) {
                    resultToChance.put(this.outputs.size() - 1, chances.get(i));
                }
                if (focus != null && NEIServerUtils.areStacksSameTypeCrafting(focus, v))
                    this.focus = new Point(38 + 18 * col, 2 + 18 * row);
                col++;
                if (col > 6) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    @Override
    public String getRecipeName() {
        return "Sieve";
    }

    @Override
    public String getGuiTexture() {
        return "exnihilo:textures/sieveNEI.png";
    }

    private void addCached(List<ItemStack> variations, MeshType meshType, ItemStack base, ItemStack focus,
        List<SiftingResult> chances) {
        if (variations.size() > 21) {
            List<List<ItemStack>> parts = new ArrayList<>();
            int size = variations.size();
            for (int i = 0; i < size; i += 21)
                parts.add(new ArrayList<>(variations.subList(i, Math.min(size, i + 21))));
            int partIndex = 0;
            for (List<ItemStack> part : parts) {
                int start = partIndex * 21;
                int end = Math.min(start + 21, chances.size());
                List<SiftingResult> partChances = chances.subList(start, end);
                this.arecipes.add(new CachedSieveRecipe(part, meshType, base, focus, partChances));
                partIndex++;
            }
        } else {
            this.arecipes.add(new CachedSieveRecipe(variations, meshType, base, focus, chances));
        }
    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 56);
        Point focus = ((CachedSieveRecipe) this.arecipes.get(recipeIndex)).focus;
        if (focus != null) GuiDraw.drawTexturedModalRect(focus.x, focus.y, 166, 0, 18, 18);
    }

    @Override
    public int recipiesPerPage() {
        return 4;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects
            .add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(18, 22, 18, 14), "exnihilo.sieve"));
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... results) {
        if (outputID.equals("exnihilo.sieve")) {
            for (MeshType meshType : MeshType.getValues()) {
                if (meshType == MeshType.NONE) continue;
                for (ItemInfo ii : SieveRegistry.getSiftables()
                    .get(meshType)
                    .keySet()) {
                    ItemStack inputStack = ii.getStack();
                    ArrayList<ItemStack> resultStacks = new ArrayList<>();
                    ArrayList<SiftingResult> chances = new ArrayList<>();
                    for (SiftingResult s : SieveRegistry.getSiftingOutput(ii, meshType)) {
                        resultStacks.add(new ItemStack(s.drop.getItem(), 1, s.drop.getMeta()));
                        chances.add(s);
                    }
                    addCached(resultStacks, meshType, inputStack, null, chances);
                }
            }
        } else {
            super.loadCraftingRecipes(outputID, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Map.Entry<MeshType, ArrayList<ItemInfo>> set : SieveRegistry.getSources(result)
            .entrySet()) {
            for (ItemInfo ii : set.getValue()) {
                ArrayList<SiftingResult> results = SieveRegistry
                    .getSiftingOutput(Block.getBlockFromItem(ii.getItem()), ii.getMeta(), set.getKey());
                if (results == null) continue;

                // Build result stacks and pair them with their SiftingResults
                ArrayList<ItemStack> resultStacks = new ArrayList<>();
                ArrayList<SiftingResult> chances = new ArrayList<>();

                for (SiftingResult sr : results) {
                    ItemStack stack = new ItemStack(sr.drop.getItem(), 1, sr.drop.getMeta());
                    resultStacks.add(stack);
                    chances.add(sr);
                }

                addCached(resultStacks, set.getKey(), ii.getStack(), result, chances);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (Block.getBlockFromItem(ingredient.getItem()) == Blocks.air) return;
        for (MeshType meshType : MeshType.getValues()) {
            if (meshType == MeshType.NONE) continue;
            ArrayList<SiftingResult> results = SieveRegistry
                .getSiftingOutput(Block.getBlockFromItem(ingredient.getItem()), ingredient.getItemDamage(), meshType);
            if (results == null) continue;

            // Build result stacks and pair them with their SiftingResults
            ArrayList<ItemStack> resultStacks = new ArrayList<>();
            ArrayList<SiftingResult> chances = new ArrayList<>();

            for (SiftingResult sr : results) {
                ItemStack stack = new ItemStack(sr.drop.getItem(), 1, sr.drop.getMeta());
                resultStacks.add(stack);
                chances.add(sr);
            }

            addCached(resultStacks, meshType, ingredient, ingredient, chances);
        }
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> guiRecipe, ItemStack itemStack, List<String> currenttip,
        int recipe) {
        CachedSieveRecipe crecipe = (CachedSieveRecipe) this.arecipes.get(recipe);
        Point mouse = GuiDraw.getMousePosition();
        Point offset = guiRecipe.getRecipePosition(recipe);
        Point relMouse = new Point(
            mouse.x - (guiRecipe.width - 166) / 2 - offset.x,
            mouse.y - (guiRecipe.height - (56 * this.recipiesPerPage())) / 2 - offset.y);

        if (itemStack != null && relMouse.x > 32
            && mouse.y > offset.y + 32
            && mouse.y < (((guiRecipe.height - (56 * this.recipiesPerPage())) / 2 + offset.y) + 56) + 32) {

            int outputIndex = -1;
            for (int i = 0; i < crecipe.outputs.size(); i++) {
                if (NEIServerUtils.areStacksSameTypeCrafting(itemStack, crecipe.outputs.get(i).item)) {
                    outputIndex = i;
                    break;
                }
            }

            if (outputIndex >= 0) {
                SiftingResult smash = crecipe.getChanceForResult(outputIndex);
                if (smash != null) {
                    switch (smash.type) {
                        case CHANCE:
                            double chance = 100.0D / smash.paramN;
                            String chanceStr;
                            if (smash.paramN <= 0) {
                                chanceStr = "0";
                            } else if (chance >= 1) {
                                chanceStr = df.format(chance);
                            } else {
                                chanceStr = "< 1";
                            }
                            currenttip.add("\u00a77Chance: " + chanceStr + "%");
                            break;
                        case BINOMIAL:
                            // When n=1, display as chance percentage (same as ExDeorum)
                            if (smash.paramN == 1) {
                                double chancePercent = smash.paramP * 100.0D;
                                String chancePercentStr;
                                if (chancePercent >= 1) {
                                    chancePercentStr = df.format(chancePercent);
                                } else {
                                    chancePercentStr = "< 1";
                                }
                                currenttip.add("\u00a77Chance: " + chancePercentStr + "%");
                            } else {
                                double avg = smash.paramN * smash.paramP;
                                String avgStr = df.format(avg);
                                currenttip.add("\u00a77Avg. Output: " + avgStr);
                            }
                            // Always show min/max for binomial (same as ExDeorum)
                            currenttip.add("\u00a77Min: 0");
                            currenttip.add("\u00a77Max: " + smash.paramN);
                            break;
                        case FIXED:
                            // Fixed drops - no extra info needed
                            break;
                    }
                }
            }
        }
        return super.handleItemTooltip(guiRecipe, itemStack, currenttip, recipe);
    }
}
