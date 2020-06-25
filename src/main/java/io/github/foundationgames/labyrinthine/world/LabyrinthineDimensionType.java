package io.github.foundationgames.labyrinthine.world;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public class LabyrinthineDimensionType extends DimensionType {
    public LabyrinthineDimensionType(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, boolean shrunk, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, Identifier infiniburn, float ambientLight) {
        super(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, shrunk, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, infiniburn, ambientLight);
    }
    public LabyrinthineDimensionType(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, boolean shrunk, boolean hasEnderDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, BiomeAccessType biomeAccessType, Identifier infiniburn, float ambientLight) {
        super(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, shrunk, hasEnderDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, biomeAccessType, infiniburn, ambientLight);
    }
}
