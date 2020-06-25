package io.github.foundationgames.labyrinthine.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.foundationgames.labyrinthine.Labyrinthine;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.chunk.StructuresConfig;

import java.util.Optional;

public class DungeonRealmChunkGenerator extends FlatChunkGenerator {
    public DungeonRealmChunkGenerator() {
        super(createDungeonRealmGenConfig());
    }

    private static FlatChunkGeneratorConfig createDungeonRealmGenConfig() {
        FlatChunkGeneratorConfig cfg = new FlatChunkGeneratorConfig(
                new StructuresConfig(Optional.empty(), ImmutableMap.of())
        )
                .method_29965(
                        ImmutableList.of(
                                new FlatChunkGeneratorLayer(1, Blocks.BEDROCK),
                                new FlatChunkGeneratorLayer(243, Blocks.STONE),
                                new FlatChunkGeneratorLayer(1, Blocks.BEDROCK)
                        ),
                        new StructuresConfig(Optional.empty(), ImmutableMap.of())
                );
        cfg.setBiome(Labyrinthine.DUNGEON_REALM_BIOME);
        return cfg;
    }
}
