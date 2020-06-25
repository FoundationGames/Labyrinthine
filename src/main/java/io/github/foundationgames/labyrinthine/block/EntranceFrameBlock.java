package io.github.foundationgames.labyrinthine.block;

import com.google.common.base.Predicates;
import io.github.foundationgames.labyrinthine.Labyrinthine;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Objects;

public class EntranceFrameBlock extends FacingBlock {

    public static final BooleanProperty ACTIVE;
    private static BlockPattern LIT_FRAME;

    public EntranceFrameBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP).with(ACTIVE, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateState(ctx.getWorld(), ctx.getBlockPos(), this.getDefaultState().with(FACING, ctx.getSide()));
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        world.setBlockState(pos, updateState(world, pos, state));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        trySpawnPortal(world, pos);
        return updateState(world, pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        trySpawnPortal(world, pos);
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    private BlockState updateState(WorldAccess world, BlockPos pos, BlockState state) {
        BlockState newState = state;
        newState = newState.with(ACTIVE, world.getBlockState(pos.offset(state.get(FACING))).getBlock() == LabyrinthineBlocks.DUNGEON_PORTAL || world.getBlockState(pos.offset(state.get(FACING))).getBlock() == Blocks.LAVA);
        return newState;
    }

    public static BlockPattern getLitFramePattern() {
        if(LIT_FRAME == null) {
            LIT_FRAME = BlockPatternBuilder.start()
                    .aisle("XSSSX")
                    .aisle("EXXXW")
                    .aisle("EXXXW")
                    .aisle("EXXXW")
                    .aisle("XNNNX")
                    .where('X', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
                    .where('S', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(LabyrinthineBlocks.ENTRANCE_FRAME)
                            .with(ACTIVE, Predicates.equalTo(true))
                            //.with(FACING, Predicates.equalTo(Direction.SOUTH))
                    ))
                    .where('N', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(LabyrinthineBlocks.ENTRANCE_FRAME)
                            .with(ACTIVE, Predicates.equalTo(true))
                            //.with(FACING, Predicates.equalTo(Direction.NORTH))
                    ))
                    .where('E', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(LabyrinthineBlocks.ENTRANCE_FRAME)
                            .with(ACTIVE, Predicates.equalTo(true))
                            //.with(FACING, Predicates.equalTo(Direction.EAST))
                    ))
                    .where('W', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(LabyrinthineBlocks.ENTRANCE_FRAME)
                            .with(ACTIVE, Predicates.equalTo(true))
                            //.with(FACING, Predicates.equalTo(Direction.WEST))
                    ))
                    .build();
        }
        return LIT_FRAME;
    }

    private void trySpawnPortal(WorldAccess world, BlockPos pos) {
        if(world.getBlockState(pos).get(FACING) == Direction.WEST || world.getBlockState(pos).get(FACING) == Direction.NORTH) {
            BlockPattern.Result result = getLitFramePattern().searchAround(world, pos);
            if (result != null) {
                BlockPos topLeftInner = result.getFrontTopLeft().add(-3, 0, -3);
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        BlockPos bpos = topLeftInner.add(x, 0, z);
                        BlockState bstate = LabyrinthineBlocks.DUNGEON_PORTAL.getDefaultState();
                        world.setBlockState(bpos, LabyrinthineBlocks.DUNGEON_PORTAL.getStateForNeighborUpdate(bstate, Direction.UP, bstate, world, bpos, bpos), 2);
                    }
                }
            }
        }
    }

    static {
        ACTIVE = BooleanProperty.of("active");
    }
}
