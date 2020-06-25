package io.github.foundationgames.labyrinthine.block;

import io.github.foundationgames.labyrinthine.world.LabyrinthineWorldGen;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class DungeonPortalBlock extends Block {

    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty WEST;

    public DungeonPortalBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return createCuboidShape(0, 0, 0, 16, 9, 16);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateState(ctx.getWorld(), ctx.getBlockPos(), this.getDefaultState());
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.setBlockState(pos, updateState(world, pos, state));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return updateState(world, pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals() && entity.world instanceof ServerWorld) {
            MinecraftServer server = ((ServerWorld)entity.world).getServer();
            RegistryKey<World> worldKey = entity.world.getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY ? World.OVERWORLD : LabyrinthineWorldGen.DUNGEON_REALM_KEY;
            ServerWorld dimension = server.getWorld(worldKey);
            FabricDimensions.teleport(entity, dimension, LabyrinthineWorldGen::placeEntityOnExitPlatformOrSpawn);
        }
    }

    private BlockState updateState(WorldAccess world, BlockPos pos, BlockState state) {
        BlockState newState = state;
        newState = newState.with(NORTH, world.getBlockState(pos.north()).getBlock() instanceof DungeonPortalBlock);
        newState = newState.with(SOUTH, world.getBlockState(pos.south()).getBlock() instanceof DungeonPortalBlock);
        newState = newState.with(EAST, world.getBlockState(pos.east()).getBlock() instanceof DungeonPortalBlock);
        newState = newState.with(WEST, world.getBlockState(pos.west()).getBlock() instanceof DungeonPortalBlock);
        return newState;
    }

    static {
        NORTH = BooleanProperty.of("north");
        SOUTH = BooleanProperty.of("south");
        EAST = BooleanProperty.of("east");
        WEST = BooleanProperty.of("west");
    }
}
