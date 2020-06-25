package io.github.foundationgames.labyrinthine.mixin;

import io.github.foundationgames.labyrinthine.event.LabyrinthineEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
    public void stopExplosionSometimes(boolean boo, CallbackInfo ci) {
        if(!LabyrinthineEvents.LABYRINTH_COMPLETE) ci.cancel();
    }
}
