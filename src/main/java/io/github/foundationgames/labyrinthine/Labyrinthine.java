package io.github.foundationgames.labyrinthine;

import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.event.LabyrinthineEvents;
import io.github.foundationgames.labyrinthine.world.DungeonRealmBiome;
import io.github.foundationgames.labyrinthine.world.LabyrinthineWorldGen;
import io.github.foundationgames.labyrinthine.item.LabyrinthineItems;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class Labyrinthine implements ModInitializer {

    public static final ItemGroup LABYRINTHINE_ITEMS = FabricItemGroupBuilder.build(Utilz.id("labyrinthine_items"), () -> new ItemStack(LabyrinthineBlocks.FUNGLOW));

    public static final Biome DUNGEON_REALM_BIOME = Registry.register(Registry.BIOME, Utilz.id("dungeon_realm"), new DungeonRealmBiome());

    @Override
    public void onInitialize() {
        LabyrinthineWorldGen.init();
        LabyrinthineBlocks.init();
        LabyrinthineItems.init();
        LabyrinthineEvents.init();
        System.out.println("Hello World from Labyrinthine");
    }
}
