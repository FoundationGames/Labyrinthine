package io.github.foundationgames.labyrinthine.world;

import com.mojang.brigadier.CommandDispatcher;
import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.event.LabyrinthCompletedPersistentState;
import io.github.foundationgames.labyrinthine.mixin.FlatChunkGeneratorConfigAccessor;
import io.github.foundationgames.labyrinthine.mixin.StructureFeatureAccessor;
import io.github.foundationgames.labyrinthine.util.Mathz;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class LabyrinthineWorldGen {

    public static final RegistryKey<DimensionOptions> DUNGEON_REALM_OPTIONS_KEY = RegistryKey.of(Registry.DIMENSION_OPTIONS, Utilz.id("dungeon_realm"));
    public static final RegistryKey<DimensionType> DUNGEON_REALM_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, Utilz.id("dungeon_realm"));
    public static final RegistryKey<World> DUNGEON_REALM_KEY = RegistryKey.of(Registry.DIMENSION, Utilz.id("dungeon_realm"));

    public static void init() {
        FabricDimensions.registerDefaultPlacer(DUNGEON_REALM_KEY, LabyrinthineWorldGen::placeEntityOnExitPlatformOrSpawn);
        Registry.BIOME.forEach(biome -> { if(biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) biome.addStructureFeature(PORTAL_ROOM_STRUCTURE.configure(new DefaultFeatureConfig())); });
        StructureFeature.STRUCTURES.put("labyrinthine_labyrinth", LABYRINTH_STRUCTURE);
        StructureFeatureAccessor.getStructureGenStepMap().put(LABYRINTH_STRUCTURE, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        StructureFeature.STRUCTURES.put("dungeon_portal_room", PORTAL_ROOM_STRUCTURE);
        StructureFeatureAccessor.getStructureGenStepMap().put(PORTAL_ROOM_STRUCTURE, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        FlatChunkGeneratorConfigAccessor.getStructureFeatureMap().put(LABYRINTH_STRUCTURE, LABYRINTH_STRUCTURE.configure(new StructurePoolFeatureConfig(Utilz.id("treasure_rooms"), 38)));
    }

    public static BlockPattern.TeleportTarget placeEntityOnExitPlatformOrSpawn(Entity teleported, ServerWorld dimension, Direction portalDir, double horizontalOffset, double verticalOffset) {
        if (dimension.getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY) {
            if (teleported != null) {
                BlockPos result = Utilz.searchForBlock2dOrDefault(teleported.getBlockPos(), LabyrinthineBlocks.EXIT_PLATFORM.getDefaultState(), dimension, new BlockPos(0, 75, 0), 212);
                return new BlockPattern.TeleportTarget(Mathz.bp2v3d(result.up()).add(0.5, 0, 0.5), teleported.getVelocity(), (int)teleported.yaw);
            }
            return new BlockPattern.TeleportTarget(new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), 0);
        } else if(dimension.getRegistryKey() == World.OVERWORLD) {
            BlockPos spawnPos = dimension.getSpawnPos();
            if(teleported instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)teleported;
                if(player.getSleepingPosition().isPresent()) {
                    spawnPos = player.getSleepingPosition().get();
                }
            }
            return new BlockPattern.TeleportTarget(Mathz.bp2v3d(spawnPos), teleported.getVelocity(), (int)teleported.yaw);
        }
        return new BlockPattern.TeleportTarget(new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), 0);
    }

    public static final StructureFeature<StructurePoolFeatureConfig> LABYRINTH_STRUCTURE = Registry.register(
            Registry.STRUCTURE_FEATURE,
            Utilz.id("labyrinth"),
            new LabyrinthFeature()
    );

    public static final StructurePieceType LABYRINTH_PIECE = Registry.register(
            Registry.STRUCTURE_PIECE,
            Utilz.id("labyrinth_piece"),
            LabyrinthGenerator.Piece::new
    );

    public static final StructureFeature<DefaultFeatureConfig> PORTAL_ROOM_STRUCTURE = Registry.register(
            Registry.STRUCTURE_FEATURE,
            Utilz.id("portal_room"),
            new DungeonPortalRoomFeature()
    );

    public static final StructurePieceType PORTAL_ROOM_PIECE = Registry.register(
            Registry.STRUCTURE_PIECE,
            Utilz.id("portal_room_piece"),
            DungeonPortalRoomFeature.Piece::new
    );
}
