package io.github.foundationgames.labyrinthine.item;

import io.github.foundationgames.labyrinthine.Labyrinthine;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class LabyrinthineItems {

    //HOW TO ADD AN ITEM!!!
    //Write this code below all this gray text:

    //public static final Item MY_ITEM = register("my_item", new LabyrinthineItem(new Item.Settings().group(MY_GROUP)));

    //Replace MY_ITEM with the name of your item (underscores and full caps because yes; ex: COOKED_BEEF)
    //Replace MY_GROUP with the item group it should be in (Probably do Labyrinthine.LABYRINTHINE_ITEMS)
    //Replace the "my_item" with the item id of your item (like "cooked_beef")
    //If you don't want a boring old regular useless item, instead of creating a "new Item()", create something else like a "new SwordItem()" (which may require more options like a tool material, beware)
    //You can even make a custom item class for a super unique item!
    //Textures, models, and properly translated names are done in resource pack

    //DM me if you have confusion

    //Here is an example: A rune item!
    public static final Item RUNE = register("rune", new LabyrinthineItem(new Item.Settings().group(Labyrinthine.LABYRINTHINE_ITEMS)));

    // V Add more items below this line of text! V

    //IGNORE THESE, NO SON IMPORTANTE
    public static void init() {
    }
    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, Utilz.id(id), item);
    }
}
