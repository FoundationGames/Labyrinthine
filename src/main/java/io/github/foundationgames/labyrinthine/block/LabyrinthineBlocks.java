package io.github.foundationgames.labyrinthine.block;

import io.github.foundationgames.labyrinthine.Labyrinthine;
import io.github.foundationgames.labyrinthine.block.entity.DungeonSpawnerBlockEntity;
import io.github.foundationgames.labyrinthine.block.entity.MobSpawnBlockEntity;
import io.github.foundationgames.labyrinthine.block.entity.PrismBlockEntity;
import io.github.foundationgames.labyrinthine.item.LabyrinthineBlockItem;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class LabyrinthineBlocks {

    public static final Block DUNGEON_PORTAL = registerBlockOnly("dungeon_portal", new DungeonPortalBlock(FabricBlockSettings.of(Material.PORTAL, MaterialColor.GOLD).strength(-1.0F, 3600000.0F).dropsNothing().lightLevel(10).nonOpaque().noCollision()));
    public static final Block ENTRANCE_FRAME = register("entrance_frame", Labyrinthine.LABYRINTHINE_ITEMS, new EntranceFrameBlock(FabricBlockSettings.copy(Blocks.BEDROCK).lightLevel((state) -> state.get(EntranceFrameBlock.ACTIVE) ? 5 : 0)));
    public static final Block EXIT_PLATFORM = register("exit_platform", Labyrinthine.LABYRINTHINE_ITEMS, new ConfigurableBlock(FabricBlockSettings.copy(Blocks.BEDROCK).lightLevel((state) -> 2)));
    public static final Block FUNGLOW = register("funglow", Labyrinthine.LABYRINTHINE_ITEMS, new FunglowBlock(FabricBlockSettings.copy(Blocks.WARPED_FUNGUS).emissiveLighting((state, world, pos) -> true)));
    public static final Block TAINTED_STONE_BRICKS = register("tainted_stone_bricks", Labyrinthine.LABYRINTHINE_ITEMS, new ConfigurableBlock(FabricBlockSettings.copy(Blocks.MOSSY_STONE_BRICKS)));
    public static final Block TAINTED_STONE_BRICK_STAIRS = register("tainted_stone_brick_stairs", Labyrinthine.LABYRINTHINE_ITEMS, new Utilz.StairBlock(TAINTED_STONE_BRICKS.getDefaultState(), FabricBlockSettings.copy(Blocks.STONE_BRICK_STAIRS)));
    public static final Block FUNGLOWING_STONE_BRICKS = register("funglowing_stone_bricks", Labyrinthine.LABYRINTHINE_ITEMS, new ConfigurableBlock(FabricBlockSettings.copy(Blocks.MOSSY_STONE_BRICKS)));
    public static final Block FUNGLOWING_STONE_BRICK_STAIRS = register("funglowing_stone_brick_stairs", Labyrinthine.LABYRINTHINE_ITEMS, new Utilz.StairBlock(FUNGLOWING_STONE_BRICKS.getDefaultState(), FabricBlockSettings.copy(Blocks.STONE_BRICK_STAIRS)));
    public static final Block FUNGLOWING_CHISELED_STONE_BRICKS = register("funglowing_chiseled_stone_bricks", Labyrinthine.LABYRINTHINE_ITEMS, new ConfigurableBlock(FabricBlockSettings.copy(Blocks.MOSSY_STONE_BRICKS)));
    public static final Block DUNGEON_SPAWNER = register("dungeon_spawner", Labyrinthine.LABYRINTHINE_ITEMS, new DungeonSpawnerBlock(FabricBlockSettings.copy(Blocks.SPAWNER).strength(-1.0F, 3600000.0F)));
    public static final Block MOB_SPAWN = register("mob_spawn", new MobSpawnBlock(FabricBlockSettings.copy(Blocks.SKELETON_SKULL).strength(-1.0F, 3600000.0F)));
    public static final Block PRISM = new PrismBlock(FabricBlockSettings.copy(Blocks.IRON_BARS)); //UNUSED
    public static final Block COIN_STACK = register("coin_stack", Labyrinthine.LABYRINTHINE_ITEMS, new CoinStackBlock(FabricBlockSettings.copy(Blocks.WHITE_WOOL).strength(0.03f, 0.2f).sounds(BlockSoundGroup.CHAIN)));
    public static final Block SPELL_BINDER = register("spell_binder", new SpellBinderBlock(FabricBlockSettings.copy(DUNGEON_SPAWNER)));

    public static final BlockEntityType<DungeonSpawnerBlockEntity> DUNGEON_SPAWNER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utilz.id("dungeon_spawner"), BlockEntityType.Builder.create(DungeonSpawnerBlockEntity::new, DUNGEON_SPAWNER).build(null));
    public static final BlockEntityType<MobSpawnBlockEntity> MOB_SPAWN_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utilz.id("mob_spawn"), BlockEntityType.Builder.create(MobSpawnBlockEntity::new, MOB_SPAWN).build(null));
    public static final BlockEntityType<PrismBlockEntity> PRISM_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utilz.id("prism"), BlockEntityType.Builder.create(PrismBlockEntity::new, PRISM).build(null));

    public static void init() {
    }
    public static Block registerBlockOnly(String id, Block block) {
        return Registry.register(Registry.BLOCK, Utilz.id(id), block);
    }
    public static Block register(String id, ItemGroup group, Block block) {
        Registry.register(Registry.ITEM, Utilz.id(id), new LabyrinthineBlockItem(block, new Item.Settings().group(group)));
        return registerBlockOnly(id, block);
    }
    public static Block register(String id, Block block) {
        Registry.register(Registry.ITEM, Utilz.id(id), new LabyrinthineBlockItem(block, new Item.Settings()));
        return registerBlockOnly(id, block);
    }
}
