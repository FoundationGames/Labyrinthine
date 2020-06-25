package io.github.foundationgames.labyrinthine.util;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class Utilz {
    public static final String MOD_ID = "labyrinthine";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static void spawnCritParticles(World world, BlockPos pos) {
        Random rng = new Random();
        for (int i = 0; i < 15; i++) {
            double dx = (rng.nextDouble() - 0.5) * 1.5;
            double dy = rng.nextDouble();
            double dz = (rng.nextDouble() - 0.5) * 1.5;
            double ox = (rng.nextDouble() - 0.5) * 0.4;
            double oy = (rng.nextDouble() - 0.5) * 0.4;
            double oz = (rng.nextDouble() - 0.5) * 0.4;
            world.addParticle(ParticleTypes.CRIT, pos.getX()+0.5+ox, pos.getY()+0.5+oy, pos.getZ()+0.5+oz, dx, dy, dz);
        }
    }

    public static void spawnEnchParticles(World world, BlockPos pos) {
        Random rng = new Random();
        for (int i = 0; i < 15; i++) {
            double dx = (rng.nextDouble() - 0.5) * 1.5;
            double dy = rng.nextDouble();
            double dz = (rng.nextDouble() - 0.5) * 1.5;
            double ox = (rng.nextDouble() - 0.5) * 0.4;
            double oy = (rng.nextDouble() - 0.5) * 0.4;
            double oz = (rng.nextDouble() - 0.5) * 0.4;
            world.addParticle(ParticleTypes.ENCHANTED_HIT, pos.getX()+0.5+ox, pos.getY()+0.5+oy, pos.getZ()+0.5+oz, dx, dy, dz);
        }
    }

    public static BlockPos searchForBlockOrDefault(BlockPos defaultPos, BlockState query, ServerWorld world, BlockPos reference, int radius) {
        int searchwidth = (radius * 2) + 1;
        BlockPos searching;
        List<BlockPos> results = Lists.newArrayList();
        for (int x = -radius; x < searchwidth; x++) {
            for (int y = -radius; y < searchwidth; y++) {
                for (int z = -radius; z < searchwidth; z++) {
                    searching = new BlockPos(x+reference.getX(), y+reference.getY(), z+reference.getZ());
                    if(world.getBlockState(searching).equals(query)) {
                        results.add(searching);
                    }
                }
            }
        }
        if(results.size() > 0) {
            return results.get(0);
        }
        return defaultPos;
    }

    public static BlockPos searchForBlock2dOrDefault(BlockPos defaultPos, BlockState query, ServerWorld world, BlockPos reference, int radius) {
        int searchwidth = (radius * 2) + 1;
        int y = reference.getY();
        if(y > 255) y = 255;
        if(y < 0) y = 0;
        BlockPos searching;
        List<BlockPos> results = Lists.newArrayList();
        for (int x = -radius; x < searchwidth; x++) {
            for (int z = -radius; z < searchwidth; z++) {
                searching = new BlockPos(x+reference.getX(), y, z+reference.getZ());
                if(world.getBlockState(searching).equals(query)) {
                    results.add(searching);
                    break;
                }
            }
        }
        if(results.size() > 0) {
            return results.get(0);
        }
        return defaultPos;
    }

    public static class StairBlock extends StairsBlock {
        public StairBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }
}
