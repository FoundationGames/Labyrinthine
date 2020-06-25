package io.github.foundationgames.labyrinthine.world;

import io.github.foundationgames.labyrinthine.util.Utilz;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.NopeSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class DungeonRealmBiome extends Biome {
    public DungeonRealmBiome() {
        super(
                new Biome.Settings()
                .configureSurfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG)
                .precipitation(Precipitation.NONE)
                .category(Category.NONE)
                .depth(1.0F)
                .scale(1.0F)
                .temperature(0.5F)
                .downfall(0.5F)
                .effects(new BiomeEffects.Builder().waterColor(0x00ffe5).fogColor(0x00175c).waterFogColor(0x00ffe5).build())
                .parent(null)
        );
        this.addStructureFeature(LabyrinthineWorldGen.LABYRINTH_STRUCTURE.configure(new StructurePoolFeatureConfig(Utilz.id("treasure_rooms"), 20)));
    }

    @Override
    public int getSkyColor() {
        return 0x000000;
    }

    @Override
    public int getFoliageColor() {
        return 0x0b8a2d;
    }
}
