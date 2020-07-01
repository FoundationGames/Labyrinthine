package io.github.foundationgames.labyrinthine.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

public class LabyrinthSpawnPersistentState extends PersistentState {

    public BlockPos spawnPos = null;

    public LabyrinthSpawnPersistentState(String key) {
        super(key);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        CompoundTag posTag = tag.getCompound("SpawnPos");
        spawnPos = new BlockPos(
                posTag.getInt("x"),
                posTag.getInt("y"),
                posTag.getInt("z")
        );
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag posTag = new CompoundTag();
        posTag.putInt("x", spawnPos.getX());
        posTag.putInt("y", spawnPos.getY());
        posTag.putInt("z", spawnPos.getZ());
        tag.put("SpawnPos", posTag);
        return tag;
    }
}
