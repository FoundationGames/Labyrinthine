package io.github.foundationgames.labyrinthine;

import io.github.foundationgames.labyrinthine.block.LabyrinthineBlocks;
import io.github.foundationgames.labyrinthine.block.entity.render.DungeonSpawnerBlockEntityRenderer;
import io.github.foundationgames.labyrinthine.block.entity.render.PrismBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class LabyrinthineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Hello Client World from Labyrinthine");
        BlockRenderLayerMap.INSTANCE.putBlock(LabyrinthineBlocks.DUNGEON_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(LabyrinthineBlocks.DUNGEON_SPAWNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LabyrinthineBlocks.FUNGLOW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LabyrinthineBlocks.SPELL_BINDER, RenderLayer.getCutout());

        BlockEntityRendererRegistry.INSTANCE.register(LabyrinthineBlocks.DUNGEON_SPAWNER_ENTITY, DungeonSpawnerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(LabyrinthineBlocks.PRISM_ENTITY, PrismBlockEntityRenderer::new);
    }
}
