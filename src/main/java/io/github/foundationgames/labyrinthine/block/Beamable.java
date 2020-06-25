package io.github.foundationgames.labyrinthine.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public interface Beamable {
    void onBeamed(BlockState state, WorldAccess world, BlockPos pos, BlockHitResult hit);
}
