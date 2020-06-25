package io.github.foundationgames.labyrinthine.block;

import io.github.foundationgames.labyrinthine.event.LabyrinthCompletedPersistentState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

public class SpellBinderBlock extends Block {
    public SpellBinderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if (!world.isReceivingRedstonePower(pos)) {
            world.removeBlock(pos, false);
            triggerLabyrinthEnd(state, world, pos);
        }
    }

    private void triggerLabyrinthEnd(BlockState state, World world, BlockPos pos) {
        if (world instanceof ServerWorld) {
            LabyrinthCompletedPersistentState pstate = ((ServerWorld)world).getPersistentStateManager().getOrCreate(() -> new LabyrinthCompletedPersistentState("labyrinthCompleted"), "labyrinthCompleted");
            pstate.labyrinthComplete = true;
            pstate.setDirty(true);
        }
    }
}