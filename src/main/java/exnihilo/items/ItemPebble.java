package exnihilo.items;

import net.minecraft.item.Item;

public class ItemPebble extends Item {

    private final String pebbleType;

    public ItemPebble(String pebbleType) {
        this.pebbleType = pebbleType;
        setUnlocalizedName("exnihilo.pebble_" + pebbleType);
        setTextureName("exnihilo:pebble_" + pebbleType);
    }

    public String getPebbleType() {
        return pebbleType;
    }
}
