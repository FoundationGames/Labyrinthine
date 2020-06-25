package io.github.foundationgames.labyrinthine.mixin;

import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.block.entity.DungeonSpawnerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {

    @Shadow public EntityType<?> getEntityType(CompoundTag tag) { return null; }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void changeDungeonSpawner(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = context.getWorld().getBlockState(blockPos);
        if (blockState.isOf(LabyrinthineBlocks.DUNGEON_SPAWNER)) {
            BlockEntity blockEntity = context.getWorld().getBlockEntity(blockPos);
            if (blockEntity instanceof DungeonSpawnerBlockEntity) {
                EntityType<?> entityType = this.getEntityType(itemStack.getTag());
                ((DungeonSpawnerBlockEntity)blockEntity).updateEntity(entityType);
                blockEntity.markDirty();
                itemStack.decrement(1);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
