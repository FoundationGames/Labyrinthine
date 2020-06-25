package io.github.foundationgames.labyrinthine.block;

import io.github.foundationgames.labyrinthine.block.entity.PrismBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class PrismBlock extends Block implements BlockEntityProvider, Beamable {

    private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(4, 0, 4, 12, 3, 12), createCuboidShape(6, 3, 6, 10, 4, 10), createCuboidShape(4, 4, 4, 12, 12, 12));

    public PrismBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new PrismBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.isSneaking()) {
            if(world.getBlockEntity(pos) instanceof PrismBlockEntity && world.getBlockEntity(pos) != null) {
                PrismBlockEntity prism = ((PrismBlockEntity)world.getBlockEntity(pos));
                prism.castBeam(world, state);
                return ActionResult.SUCCESS;
            }
        }
        if(world.getBlockEntity(pos) instanceof PrismBlockEntity && world.getBlockEntity(pos) != null) {
            PrismBlockEntity prism = ((PrismBlockEntity)world.getBlockEntity(pos));
            prism.rotate(90);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onBeamed(BlockState state, WorldAccess world, BlockPos pos, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof PrismBlockEntity && world.getBlockEntity(pos) != null) {
            PrismBlockEntity prism = ((PrismBlockEntity)world.getBlockEntity(pos));
            if(!prism.isCastingBeam()) prism.castBeam(world, state);
        }
    }
}
