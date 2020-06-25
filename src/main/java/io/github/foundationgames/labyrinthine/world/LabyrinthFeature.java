package io.github.foundationgames.labyrinthine.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class LabyrinthFeature extends StructureFeature<StructurePoolFeatureConfig> {

    public LabyrinthFeature() {
        super(StructurePoolFeatureConfig.CODEC);
    }

    @Override
    public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, StructurePoolFeatureConfig featureConfig) {
        return chunkPos.equals(new ChunkPos(0, 0));
    }

    @Override
    public String getName() {
        return "labyrinthine_labyrinth";
    }

    public static class Start extends StructureStart<StructurePoolFeatureConfig> {
        public Start(StructureFeature<StructurePoolFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
            super(feature, chunkX, chunkZ, box, references, seed);
        }

        @Override
        public void init(ChunkGenerator chunkGenerator, StructureManager structureManager, int x, int z, Biome biome, StructurePoolFeatureConfig featureConfig) {
            LabyrinthGenerator.addPieces(chunkGenerator, structureManager, featureConfig.getStartPool(), featureConfig.size, random, children);
            setBoundingBoxFromChildren();
        }
    }

    static {
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        Utilz.id("maze_sections"),
                        Utilz.id("maze_terminators"),
                        ImmutableList.of(
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_straight_1"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_straight_2"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_straight_3"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_straight_4"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_corner_1"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_corner_2"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_corner_3"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_corner_4"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_fork_1"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_fork_2"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_fork_3"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_fork_4"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_cross_1"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_cross_2"), 2),
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_start_room"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        Utilz.id("treasure_rooms"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                Pair.of(new SinglePoolElement("labyrinthine:treasure_rooms/throne_room_1"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        Utilz.id("maze_terminators"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                Pair.of(new SinglePoolElement("labyrinthine:maze_sections/maze_start_room"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }
}
