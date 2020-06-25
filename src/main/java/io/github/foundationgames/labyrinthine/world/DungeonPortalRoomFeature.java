package io.github.foundationgames.labyrinthine.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class DungeonPortalRoomFeature extends StructureFeature<DefaultFeatureConfig> {
    public DungeonPortalRoomFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig featureConfig) {
        return chunkRandom.nextInt(800) == 2;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
            super(feature, chunkX, chunkZ, box, references, seed);
        }

        @Override
        public void init(ChunkGenerator chunkGenerator, StructureManager structureManager, int x, int z, Biome biome, DefaultFeatureConfig featureConfig) {
            StructurePoolBasedGenerator.addPieces(Utilz.id("portal_rooms"), 2, Piece::new, chunkGenerator, structureManager, new BlockPos(x * 16, chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE) / 2, z * 16), children, random, false, false);
            setBoundingBoxFromChildren();
        }
    }

    @Override
    public String getName() {
        return "dungeon_portal_room";
    }

    public static class Piece extends PoolStructurePiece {
        public Piece(StructureManager structureManager, StructurePoolElement structurePoolElement, BlockPos blockPos, int i, BlockRotation blockRotation, BlockBox blockBox) {
            super(LabyrinthineWorldGen.PORTAL_ROOM_PIECE, structureManager, structurePoolElement, blockPos, i, blockRotation, blockBox);
        }

        public Piece(StructureManager structureManager, CompoundTag compoundTag) {
            super(structureManager, compoundTag, LabyrinthineWorldGen.PORTAL_ROOM_PIECE);
        }
    }

    static {
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        Utilz.id("portal_rooms"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                Pair.of(new SinglePoolElement("labyrinthine:portal_rooms/portal_room_overworld_1"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }
}
