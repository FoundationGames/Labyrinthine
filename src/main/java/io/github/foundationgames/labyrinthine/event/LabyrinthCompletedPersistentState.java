package io.github.foundationgames.labyrinthine.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

public class LabyrinthCompletedPersistentState extends PersistentState {

    public boolean labyrinthComplete = false;

    public LabyrinthCompletedPersistentState(String key) {
        super(key);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        labyrinthComplete = tag.getBoolean("LabyrinthComplete");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putBoolean("LabyrinthComplete", labyrinthComplete);
        return tag;
    }
}
