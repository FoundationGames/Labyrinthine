package io.github.foundationgames.labyrinthine.mixin;

import io.github.foundationgames.labyrinthine.event.LabyrinthineEvents;
import io.github.foundationgames.labyrinthine.world.LabyrinthineWorldGen;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow public World world;

    @Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
    public void stopExplosionSometimes(boolean boo, CallbackInfo ci) {
        if(!LabyrinthineEvents.LABYRINTH_COMPLETE && world.getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY) ci.cancel();
    }
}
