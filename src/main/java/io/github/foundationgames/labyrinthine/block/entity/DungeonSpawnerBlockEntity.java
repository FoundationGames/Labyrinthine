package io.github.foundationgames.labyrinthine.block.entity;

import io.github.foundationgames.labyrinthine.block.Beamable;
import io.github.foundationgames.labyrinthine.block.DungeonSpawnerBlock;
import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.mixin.MobSpawnerLogicAccessor;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Function;

public class DungeonSpawnerBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {

    private final double maxHealth = 200;
    private double health = maxHealth;

    private final int maxHurtTime = 10;
    private int hurtTime = 0;

    private final DungeonSpawnerBlockEntity self = this;

    private final MobSpawnerLogic logic = new MobSpawnerLogic() {
        public void sendStatus(int status) {
            self.world.addSyncedBlockEvent(self.pos, Blocks.SPAWNER, status, 0);
        }

        @Override
        public World getWorld() {
            return self.world;
        }

        @Override
        public BlockPos getPos() {
            return self.pos;
        }

        @Override
        public void setSpawnEntry(MobSpawnerEntry spawnEntry) {
            super.setSpawnEntry(spawnEntry);
            if (this.getWorld() != null) {
                updateRender(this);
            }
        }
    };

    public DungeonSpawnerBlockEntity() {
        super(LabyrinthineBlocks.DUNGEON_SPAWNER_ENTITY);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.logic.fromTag(tag);
        health = tag.getDouble("Health");
        hurtTime = tag.getInt("HurtTime");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        this.logic.toTag(tag);
        tag.putDouble("Health", health);
        tag.putInt("HurtTime", hurtTime);
        return tag;
    }

    public boolean updateHealth(double amount, Optional<LivingEntity> entity) {
        if(hurtTime == 0) {
            world.setBlockState(pos, world.getBlockState(pos).with(DungeonSpawnerBlock.HIT, true));
            if (health + amount < maxHealth) {
                health += amount;
            } else health = maxHealth;
            if (health + amount <= 0) {
                health = 0;
                this.die(entity);
            }
            hurtTime = maxHurtTime;
            MobSpawnerLogicAccessor logicAccess = ((MobSpawnerLogicAccessor)logic);
            int delayShortener = (int)(-amount * 20.2);
            int spawnCount = (int)(Math.abs(amount) * 0.7);
            logicAccess.setSpawnCount(spawnCount);
            if(logicAccess.getSpawnDelay() - delayShortener > 0) logicAccess.setSpawnDelay(logicAccess.getSpawnDelay() - delayShortener);
            else logicAccess.setSpawnDelay(1);
            //System.out.println(logicAccess.getSpawnDelay());
            return true;
        }
        return false;
    }

    private void die(Optional<LivingEntity> entity) {
        if(entity.isPresent()) world.breakBlock(pos, true, entity.get());
        else world.breakBlock(pos, true);
    }

    @Override
    public void tick() {
        this.logic.update();
        if(this.hurtTime > 0) {
            this.hurtTime--;
            if(this.hurtTime <= 0) {
                world.setBlockState(pos, world.getBlockState(pos).with(DungeonSpawnerBlock.HIT, false));
            }
        }
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 1, this.toInitialChunkDataTag());
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        CompoundTag compoundTag = this.toTag(new CompoundTag());
        compoundTag.remove("SpawnPotentials");
        return compoundTag;
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        return this.logic.method_8275(type) || super.onSyncedBlockEvent(type, data);
    }

    public void updateEntity(EntityType<?> entityType) {
        this.logic.setEntityId(entityType);
        updateRender(logic);
    }

    private void updateRender(MobSpawnerLogic spawnerLogic) {
        MobSpawnerLogicAccessor logicAccess = (MobSpawnerLogicAccessor)spawnerLogic;
        logicAccess.setRenderedEntity(EntityType.loadEntityWithPassengers(logicAccess.getSpawnEntry().getEntityTag(), spawnerLogic.getWorld(), Function.identity()));
        if (logicAccess.getSpawnEntry().getEntityTag().getSize() == 1 && logicAccess.getSpawnEntry().getEntityTag().contains("id", 8) && spawnerLogic.getRenderedEntity() instanceof MobEntity) {
            ((MobEntity)spawnerLogic.getRenderedEntity()).initialize(spawnerLogic.getWorld(), spawnerLogic.getWorld().getLocalDifficulty(spawnerLogic.getRenderedEntity().getBlockPos()), SpawnReason.SPAWNER, null, null);
        }
    }

    @Override
    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    public MobSpawnerLogic getLogic() {
        return this.logic;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        this.fromTag(world.getBlockState(pos), compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return this.toTag(compoundTag);
    }
}
