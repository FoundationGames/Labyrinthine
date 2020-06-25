package io.github.foundationgames.labyrinthine.event;

import io.github.foundationgames.labyrinthine.block.DungeonSpawnerBlock;
import io.github.foundationgames.labyrinthine.world.LabyrinthineWorldGen;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class LabyrinthineEvents {

    public static boolean LABYRINTH_COMPLETE = false;

    public static void init() {
        WorldTickCallback.EVENT.register(world -> {
            if (world instanceof ServerWorld && world.getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY) {
                LabyrinthCompletedPersistentState pstate = ((ServerWorld)world).getPersistentStateManager().getOrCreate(() -> new LabyrinthCompletedPersistentState("labyrinthCompleted"), "labyrinthCompleted");
                LABYRINTH_COMPLETE = pstate.labyrinthComplete;
            }
        });
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if(world.getBlockState(pos).getBlock() instanceof DungeonSpawnerBlock) {
                ((DungeonSpawnerBlock)world.getBlockState(pos).getBlock()).attackFromPlayer(world.getBlockState(pos), world, pos, player);
            }
            else if(player.getEntityWorld().getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY && !player.isCreative() && !LABYRINTH_COMPLETE) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            Item item = player.getStackInHand(hand).getItem();
            if(world.getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY && !player.isCreative() && !LABYRINTH_COMPLETE && (item instanceof BlockItem || item instanceof BucketItem || item instanceof BoatItem || item instanceof ArmorStandItem || item instanceof BoneMealItem || item instanceof EndCrystalItem || item instanceof FlintAndSteelItem || world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof BlockWithEntity)) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            Item item = player.getStackInHand(hand).getItem();
            if(world.getRegistryKey() == LabyrinthineWorldGen.DUNGEON_REALM_KEY && !player.isCreative() && !LABYRINTH_COMPLETE && (item instanceof ChorusFruitItem || item instanceof EnderPearlItem)) {
                return TypedActionResult.fail(player.getStackInHand(hand));
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });
    }
}
