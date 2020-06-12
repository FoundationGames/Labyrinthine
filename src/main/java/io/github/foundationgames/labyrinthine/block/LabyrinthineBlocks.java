package io.github.foundationgames.labyrinthine.block;

import io.github.foundationgames.labyrinthine.Labyrinthine;
import io.github.foundationgames.labyrinthine.item.LabyrinthineBlockItem;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class LabyrinthineBlocks {

// Ignore the slashes like they don't exist

//  HOW TO ADD A BLOCK!!!!!!
//  Write this code below all this gray text:

//  public static final Block MY_BLOCK = register("my_block", Labyrinthine.LABYRINTHINE_ITEMS, new Block(<settings>));

//  public static final Block MY_BLOCK = registerBlockOnly("my_block", new Block(<settings>));

//  TOP ONE IS FOR NORMAL BLOCKS, BOTTOM IS FOR IF YOU DONT WANT IT TO HAVE AN ITEM VERSION (LIKE FIRE) YOU PROBABLY WANT TO USE THE TOP ONE
//  Replace MY_BLOCK with your block name (all caps, with underscores, ex: STONE_BRICKS)
//  Replace "my_block" with your block id , ex: "stone_bricks"
//  Replace <settings> with some block settings!
//  SETTINGS: How to make a block settings
//  WHAT IS A SETTINGS? It sets the hardness, blast res, light, and various other things

//  METHOD 1: copy the settings of a vanilla block. Replace <settings> with:
//  FabricBlockSettings.copy(Blocks.STONE)
//  Replace STONE with a valid vanilla block, it will copy the settings!

//  METHOD 2 (Harder but more customize): make your own block settings!!!! replace <settings> with:
//  FabricBlockSettings.of(Material.BAMBOO, MaterialColor.BLACK).hardness(4.20f).resistance(69.0f).lightLevel(15)
//  If you want to see all the different Materials and MaterialColors, Just type Material. or MaterialColor. and an autocomplete list will pop up
//  Because there is so much options, just type: FabricBlockSettings.of(Material.<le material>, MaterialColor.<le colour>).   and there should be an autocomplete list! (please replace le material and le colour with valid things)
//  Textures/models/name is done in resource pack

    //DM me if confuse

    //EXAMPLE: Tainted stone!!!
    public static final Block TAINTED_STONE = register("tainted_stone", Labyrinthine.LABYRINTHINE_ITEMS, new Block(FabricBlockSettings.copy(Blocks.STONE)));

    //Add more blocks below this line of text! vvv

    //Ignore
    public static void init() {
    }
    public static Block registerBlockOnly(String id, Block block) {
        return Registry.register(Registry.BLOCK, Utilz.id(id), block);
    }
    public static Block register(String id, ItemGroup group, Block block) {
        Registry.register(Registry.ITEM, Utilz.id(id), new LabyrinthineBlockItem(block, new Item.Settings().group(group)));
        return registerBlockOnly(id, block);
    }
}
