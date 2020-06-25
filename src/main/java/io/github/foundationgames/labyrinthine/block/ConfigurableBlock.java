package io.github.foundationgames.labyrinthine.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigurableBlock extends Block {

    private boolean locked = false;

    private Function<UseContext, ActionResult> onUseFunction = (ctx) -> super.onUse(ctx.state, ctx.world, ctx.pos, ctx.player, ctx.hand, ctx.hit);
    private Consumer<UpdateContext> neighborUpdateFunction = (ctx) -> super.neighborUpdate(ctx.state, ctx.world, ctx.pos, ctx.block, ctx.fromPos, ctx.notify);
    private Function<VoxelShapeContext, VoxelShape> collisionShape = (ctx) -> VoxelShapes.fullCube();
    private Function<VoxelShapeContext, VoxelShape> outlineShape = (ctx) -> VoxelShapes.fullCube();

    public ConfigurableBlock(Settings settings) {
        super(settings);
    }

    public Block whenOnUse(Function<UseContext, ActionResult> f) { if(!locked) onUseFunction = f; return this; }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return onUseFunction.apply(new UseContext(this, super.onUse(state, world, pos, player, hand, hit), state, world, pos, player, hand, hit));
    }
    public static class UseContext {
        public Block self; public ActionResult superResult;
        public BlockState state; public World world; public BlockPos pos; public PlayerEntity player; public Hand hand; public BlockHitResult hit;
        public UseContext(Block self, ActionResult superResult, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
            this.self = self; this.superResult = superResult;
            this.state = state; this.world = world; this.pos = pos; this.player = player; this.hand = hand; this.hit = hit;
        }
    }

    public Block whenNeighborUpdate(Consumer<UpdateContext> f) { if(!locked) neighborUpdateFunction = f; return this; }
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        neighborUpdateFunction.accept(new UpdateContext(this, state, world, pos, block, fromPos, notify));
    }
    public static class UpdateContext {
        public Block self;
        BlockState state; World world; BlockPos pos; Block block; BlockPos fromPos; boolean notify;
        public UpdateContext(Block self, BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
            this.self = self;
            this.state = state; this.world = world; this.pos = pos; this.block = block; this.fromPos = fromPos; this.notify = notify;
        }
    }

    public Block setOutlineShape(Function<VoxelShapeContext, VoxelShape> f) { if(!locked) outlineShape = f; return this; }
    public Block setCollisionShape(Function<VoxelShapeContext, VoxelShape> f) { if(!locked) collisionShape = f; return this; }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getOutlineShape(state, world, pos, context);
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getCollisionShape(state, world, pos, context);
    }
    public static class VoxelShapeContext {
        public Block self;
        BlockState state; BlockView world; BlockPos pos; ShapeContext shapeContext;
        public VoxelShapeContext(Block self, BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            this.self = self;
            this.state = state; this.world = world; this.pos = pos; this.shapeContext = context;
        }
    }

    public void finalize() {
        this.locked = true;
    }
}
