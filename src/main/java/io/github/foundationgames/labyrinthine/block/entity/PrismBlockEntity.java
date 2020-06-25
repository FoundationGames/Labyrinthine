package io.github.foundationgames.labyrinthine.block.entity;

import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.util.Mathz;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldAccess;

//UNUSED DUE TO TIME LIMITATIONS
public class PrismBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    private double anglePrev;
    private boolean castingBeam;
    private BeamCastPoint castPoint;

    public PrismBlockEntity() {
        super(LabyrinthineBlocks.PRISM_ENTITY);
        castPoint = new BeamCastPoint(Mathz.bp2v3d(this.pos), 0, 15);
    }

    public void rotate(double amount) {
        castPoint.rotate(amount);
        this.markDirty();
    }

    public BlockHitResult castBeam(WorldAccess world, BlockState state) {
        castingBeam = true;
        castPoint.updatePos(Mathz.bp2v3d(pos).add(0.5, 0.5, 0.5));
        return castPoint.castToBlock(world, state);
    }

    public double getAngle() {
        return castPoint.getAngle();
    }

    public double getAnglePrev() {
        return anglePrev;
    }

    public boolean hasAngleChanged() {
        return anglePrev != getAngle();
    }

    public boolean isCastingBeam() {
        return castingBeam;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        castPoint = BeamCastPoint.getFromCompound(tag.getCompound("BeamCastPoint"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("BeamCastPoint", castPoint.putToCompound());
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
        if(this.castingBeam) {
            this.castingBeam = false;
        }
    }
}
