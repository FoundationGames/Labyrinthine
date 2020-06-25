package io.github.foundationgames.labyrinthine.block.entity.render;

import io.github.foundationgames.labyrinthine.block.entity.PrismBlockEntity;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import javax.jws.WebParam;
import java.util.function.Function;

public class PrismBlockEntityRenderer extends BlockEntityRenderer<PrismBlockEntity> {

    private final PrismModel model;

    public PrismBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        model = new PrismModel(RenderLayer::getEntityCutout);
    }

    @Override
    public void render(PrismBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        Identifier texture = Utilz.id("textures/entity/prism.png");
        double modifiedAngle = ((entity.getAngle()) / 360) * 4 * (1.5707964F);
        model.setPrismAngle(modifiedAngle);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.pop();
    }

    static class PrismModel extends Model {

        private final ModelPart prismPart;

        public PrismModel(Function<Identifier, RenderLayer> layerFactory) {
            super(layerFactory);
            prismPart = new ModelPart(32, 32, 0, 0);
            prismPart.setPivot(8, 8, 8);
            prismPart.addCuboid(-4, -4, -4, 8, 8, 8);
            prismPart.pitch = 2 * (1.5707964F);
        }

        public void setPrismAngle(double angle) {
            prismPart.yaw = (float)angle;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            prismPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}
