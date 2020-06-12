package io.github.foundationgames.labyrinthine;

import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.item.LabyrinthineItems;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class Labyrinthine implements ModInitializer {

    public static final ItemGroup LABYRINTHINE_ITEMS = FabricItemGroupBuilder.build(Utilz.id("labyrinthine_items"), () -> new ItemStack(LabyrinthineItems.RUNE));

    @Override
    public void onInitialize() {
        LabyrinthineBlocks.init();
        LabyrinthineItems.init();
        System.out.println("Hello World from Labyrinthine");
    }
}
