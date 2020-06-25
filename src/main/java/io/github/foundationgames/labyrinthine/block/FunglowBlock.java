package io.github.foundationgames.labyrinthine.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;

import java.util.Random;

public class FunglowBlock extends FacingBlock {

    public static final IntProperty COUNT;

    public FunglowBlock(Settings settings) {
        super(settings.noCollision());
        setDefaultState(getDefaultState().with(COUNT, 1).with(FACING, Direction.UP));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if(state.isOf(this)) {
            return state.with(COUNT, Math.min(2, state.get(COUNT) + 1));
        }
        return getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext ctx) {
        return state.get(COUNT) < 2 && ctx.getStack().getItem() == this.asItem();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !shouldBreak((WorldAccess)world, pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(COUNT, FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getStackInHand(hand).getItem() instanceof BoneMealItem) {
            if (!world.isClient) {
                if(state.get(COUNT) == 1 && new Random().nextBoolean()) {
                    world.setBlockState(pos, state.with(COUNT, 2));
                    if(world.getBlockState(pos.offset(state.get(FACING).getOpposite())).getBlock() == Blocks.STONE_BRICKS && new Random().nextBoolean()) {
                        world.setBlockState(pos.offset(state.get(FACING).getOpposite()), LabyrinthineBlocks.TAINTED_STONE_BRICKS.getDefaultState());
                    }
                }
                world.syncWorldEvent(2005, pos, 0);
            }
            if(!player.isCreative()) player.getStackInHand(hand).decrement(1);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case UP: return createCuboidShape(3, 0, 3, 13, 8, 13);
            case DOWN: return createCuboidShape(2, 1, 2, 14, 16, 14);
            case NORTH: return createCuboidShape(4, 3, 12, 12, 13, 16);
            case SOUTH: return createCuboidShape(4, 3, 0, 12, 13, 4);
            case EAST: return createCuboidShape(0, 3, 4, 4, 13, 12);
            case WEST: return createCuboidShape(12, 3, 4, 16, 13, 12);
        }
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if(shouldBreak(world, pos, state)) {
            world.breakBlock(pos, true);
        }
    }

    private boolean shouldBreak(WorldAccess world, BlockPos pos, BlockState state) {
        if(world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSideSolidFullSquare(world, pos, state.get(FACING))) {
            return false;
        }
        return true;
    }

    static {
        COUNT = IntProperty.of("count", 1, 2);
    }
}
