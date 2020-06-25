package io.github.foundationgames.labyrinthine.item;

import io.github.foundationgames.labyrinthine.Labyrinthine;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class LabyrinthineItems {

    public static Item GOLD_COIN = register("gold_coin", new LabyrinthineItem(new Item.Settings().group(Labyrinthine.LABYRINTHINE_ITEMS)));

    public static void init() {
    }
    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, Utilz.id(id), item);
    }
}
