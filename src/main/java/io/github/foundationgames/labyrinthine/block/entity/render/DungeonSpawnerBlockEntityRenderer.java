package io.github.foundationgames.labyrinthine.block.entity.render;

import io.github.foundationgames.labyrinthine.block.entity.DungeonSpawnerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.MobSpawnerBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.MobSpawnerLogic;

public class DungeonSpawnerBlockEntityRenderer extends BlockEntityRenderer<DungeonSpawnerBlockEntity> {
    public DungeonSpawnerBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(DungeonSpawnerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5D, 0.0D, 0.5D);
        MobSpawnerLogic mobSpawnerLogic = entity.getLogic();
        Entity renderEntity = mobSpawnerLogic.getRenderedEntity();
        if (renderEntity != null) {
            float g = 0.53125F;
            float h = Math.max(renderEntity.getWidth(), renderEntity.getHeight());
            if ((double)h > 1.0D) { g /= h; }
            matrices.translate(0.0D, 0.4000000059604645D, 0.0D);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float)MathHelper.lerp(tickDelta, mobSpawnerLogic.method_8279(), mobSpawnerLogic.method_8278()) * 10.0F));
            matrices.translate(0.0D, -0.20000000298023224D, 0.0D);
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-30.0F));
            matrices.scale(g, g, g);
            MinecraftClient.getInstance().getEntityRenderManager().render(renderEntity, 0.0D, 0.0D, 0.0D, 0.0F, tickDelta, matrices, vertexConsumers, light);
        }
        matrices.pop();
    }
}
