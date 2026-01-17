package exnihilo.items.hammers;

import net.minecraft.item.Item.ToolMaterial;

public enum HammerType {

    WOOD("wood", 64, ToolMaterial.WOOD),
    STONE("stone", 128, ToolMaterial.STONE),
    IRON("iron", 256, ToolMaterial.IRON),
    GOLD("gold", 32, ToolMaterial.GOLD),
    DIAMOND("diamond", 2048, ToolMaterial.EMERALD),
    NETHERITE("netherite", 4096, ToolMaterial.EMERALD); // Use EMERALD (diamond) material for netherite

    private final String name;
    private final int durability;
    private final ToolMaterial material;

    HammerType(String name, int durability, ToolMaterial material) {
        this.name = "hammer_"+name;
        this.durability = durability;
        this.material = material;
    }

    public String getName() {
        return this.name;
    }

    public int getDurability() { return this.durability; }

    public ToolMaterial getMaterial() { return this.material; }
}
