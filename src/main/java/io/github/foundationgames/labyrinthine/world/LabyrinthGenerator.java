package io.github.foundationgames.labyrinthine.world;

import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.List;

public class LabyrinthGenerator {

    public static void addPieces(ChunkGenerator chunkGenerator, StructureManager structureManager, Identifier startPool, int size, ChunkRandom random, List<? super PoolStructurePiece> children) {
        StructurePoolBasedGenerator.addPieces(startPool, size, Piece::new, chunkGenerator, structureManager, new BlockPos(0, 69, 0), children, random, false, false);
    }

    public static class Piece extends PoolStructurePiece {
        public Piece(StructureManager structureManager, StructurePoolElement structurePoolElement, BlockPos blockPos, int i, BlockRotation blockRotation, BlockBox blockBox) {
            super(LabyrinthineWorldGen.LABYRINTH_PIECE, structureManager, structurePoolElement, blockPos, i, blockRotation, blockBox);
        }

        public Piece(StructureManager structureManager, CompoundTag compoundTag) {
            super(structureManager, compoundTag, LabyrinthineWorldGen.LABYRINTH_PIECE);
        }
    }
}
