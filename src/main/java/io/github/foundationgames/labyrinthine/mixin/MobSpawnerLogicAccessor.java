package io.github.foundationgames.labyrinthine.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobSpawnerLogic.class)
public interface MobSpawnerLogicAccessor {
    @Accessor
    int getSpawnDelay();

    @Accessor
    void setSpawnDelay(int s);

    @Accessor
    void setSpawnCount(int s);

    @Accessor
    void setRenderedEntity(Entity e);

    @Accessor
    MobSpawnerEntry getSpawnEntry();
}
