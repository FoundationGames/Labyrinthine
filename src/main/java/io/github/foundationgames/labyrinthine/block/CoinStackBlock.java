package io.github.foundationgames.labyrinthine.block;

import net.minecraft.block.*;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class CoinStackBlock extends FallingBlock implements Waterloggable {

    public static final BooleanProperty TALL;
    public static final IntProperty COUNT;
    public static final BooleanProperty WATERLOGGED;

    public CoinStackBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(TALL, false).with(COUNT, 1).with(WATERLOGGED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(COUNT, Math.min(4, blockState.get(COUNT) + 1));
        } else {
            FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            boolean bl = fluidState.getFluid() == Fluids.WATER;
            return super.getPlacementState(ctx).with(WATERLOGGED, bl);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState s, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        BlockState state = super.getStateForNeighborUpdate(s, direction, newState, world, pos, posFrom);
        return state.with(TALL, world.getBlockState(pos.up()).getBlock() == this);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TALL, COUNT, WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(TALL) ? createCuboidShape(3, 0, 3, 13, 16, 13) : createCuboidShape(3, 0, 3, 13, 8, 13);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return createCuboidShape(3, 0, 3, 13, 16, 13);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        int maximumStacks = 4;
        BlockState below = context.getWorld().getBlockState(context.getBlockPos().down());
        if(below.getBlock() == this) {
            maximumStacks = below.get(COUNT);
        }
        return context.getStack().getItem() == this.asItem() && state.get(COUNT) < maximumStacks && context.getSide() != Direction.UP || super.canReplace(state, context);
    }

    static {
        TALL = BooleanProperty.of("tall");
        COUNT = IntProperty.of("count", 1, 4);
        WATERLOGGED = Properties.WATERLOGGED;
    }
}
