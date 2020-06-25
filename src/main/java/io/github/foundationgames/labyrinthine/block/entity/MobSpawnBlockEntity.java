package io.github.foundationgames.labyrinthine.block.entity;

import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;

public class MobSpawnBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {

    public EntityType<?> entityType = EntityType.PIG;

    public MobSpawnBlockEntity() {
        super(LabyrinthineBlocks.MOB_SPAWN_ENTITY);
    }

    public void spawn() {
        entityType.spawn(world, new CompoundTag(), null, null, pos, SpawnReason.NATURAL, true, false);
        world.removeBlock(pos, false);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        entityType = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("EntityId")));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putString("EntityId", Registry.ENTITY_TYPE.getId(entityType).toString());
        return tag;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        this.fromTag(world.getBlockState(pos), compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return this.toTag(compoundTag);
    }

    @Override
    public void tick() {
        if(world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 8, true) != null) {
            //if (world.isPlayerInRange(pos.getX(), pos.getY(), pos.getZ(), 8)) {
            System.out.println("asd");
                spawn();
            //}
        }
    }
}
