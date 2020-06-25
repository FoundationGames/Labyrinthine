package io.github.foundationgames.labyrinthine.block.entity;

import io.github.foundationgames.labyrinthine.block.Beamable;
import io.github.foundationgames.labyrinthine.util.Mathz;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.WorldAccess;

public class BeamCastPoint {

    public Vec3d position;
    public double angle;
    public double maxCastDistance;

    public BeamCastPoint(Vec3d pos, double angle, double maxCastDistance) {
        this.position = pos;
        this.maxCastDistance = maxCastDistance;
        if(angle >= 360) this.angle = angle % 360;
        else if(angle < 0) this.angle = Math.abs(360 - (angle % 360));
        else this.angle = angle;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(position);
    }

    public void updatePos(Vec3d pos) {
        this.position = pos;
    }

    public BlockHitResult castToBlock(WorldAccess world, BlockState state) {
        Vec3d thisVec = this.position.add(Mathz.v3dfromYaw(new Vec3d(0, 0, 0), angle).multiply(1.5));
        Vec3d targetVec = this.position.add(Mathz.v3dfromYaw(new Vec3d(0, 0, 0), angle).multiply(maxCastDistance));
        BlockHitResult hit = world.rayTraceBlock(thisVec, targetVec, this.getBlockPos(), VoxelShapes.UNBOUNDED, state);
        if(hit != null) {
            if (world.getBlockState(hit.getBlockPos()).getBlock() instanceof Beamable && !hit.getBlockPos().equals(this.getBlockPos())) {
                ((Beamable)world.getBlockState(hit.getBlockPos()).getBlock()).onBeamed(world.getBlockState(hit.getBlockPos()), world, hit.getBlockPos(), hit);
            } else {
                world.setBlockState(new BlockPos(targetVec), Blocks.BLUE_WOOL.getDefaultState(), 2);
                System.out.println(thisVec);
                System.out.println(hit.getPos());
            }
        } else {
            world.setBlockState(new BlockPos(targetVec), Blocks.BLACK_WOOL.getDefaultState(), 2);
        }
        return hit;
    }

    public CompoundTag putToCompound() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("X", position.x);
        tag.putDouble("Y", position.y);
        tag.putDouble("Z", position.z);
        tag.putDouble("Angle", angle);
        tag.putDouble("MaxDistance", maxCastDistance);
        return tag;
    }

    public void rotate(double amount) {
        double a = this.angle + amount;
        if(a >= 360) this.angle = a % 360;
        else if(a < 0) this.angle = Math.abs(360 - (a % 360));
        else this.angle = a;
    }

    public double getAngle() {
        return angle;
    }

    public static BeamCastPoint getFromCompound(CompoundTag tag) {
        return new BeamCastPoint(new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z")), tag.getDouble("Angle"), tag.getDouble("MaxDistance"));
    }
}
