package io.github.foundationgames.labyrinthine.block;

import io.github.foundationgames.labyrinthine.block.entity.DungeonSpawnerBlockEntity;
import io.github.foundationgames.labyrinthine.util.Utilz;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Optional;
import java.util.Random;

public class DungeonSpawnerBlock extends SpawnerBlock {

    public static final BooleanProperty HIT;

    public DungeonSpawnerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(HIT, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new DungeonSpawnerBlockEntity();
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        super.onProjectileHit(world, state, hit, projectile);
        if(projectile instanceof PersistentProjectileEntity) {
            PersistentProjectileEntity projectile1 = ((PersistentProjectileEntity)projectile);
            double velocityLen = projectile.getVelocity().length();
            int damage = MathHelper.ceil(MathHelper.clamp(velocityLen * projectile1.getDamage(), 0.0D, 2.147483647E9D));
            if (projectile1.isCritical()) {
                long l = new Random().nextInt(damage / 2 + 2);
                damage = (int)Math.min(l + (long)damage, 2147483647L);
            }
            if(world.getBlockEntity(hit.getBlockPos()) instanceof DungeonSpawnerBlockEntity) {
                DungeonSpawnerBlockEntity bentity = (DungeonSpawnerBlockEntity)world.getBlockEntity(hit.getBlockPos());
                if(projectile instanceof TridentEntity) damage = 8 /* + EnchantmentHelper.getAttackDamage(((TridentEntity)projectile). , EntityGroup.DEFAULT)*/;
                boolean damaged = bentity.updateHealth(-damage, Optional.empty());
                if(damaged) {
                    world.playSound(null, hit.getBlockPos(), SoundEvents.ENTITY_GENERIC_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    if(projectile instanceof TridentEntity) {
                        projectile.setVelocity(projectile.getVelocity().multiply(-0.5D, -0.5D, -0.5D));
                    } else {
                        projectile.kill();
                    }
                }
            }
        }
    }

    public void attackFromPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        if(world.getBlockEntity(pos) instanceof DungeonSpawnerBlockEntity) {
            DungeonSpawnerBlockEntity bentity = (DungeonSpawnerBlockEntity)world.getBlockEntity(pos);
            double damage = player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            double enchDamage = EnchantmentHelper.getAttackDamage(player.getMainHandStack(), EntityGroup.DEFAULT);
            float coold = player.getAttackCooldownProgress(0.5F);
            damage *= 0.2F + coold * coold * 0.8F;
            enchDamage *= coold;
            boolean canSpecial = coold > 0.9;
            boolean canCrit = canSpecial && player.fallDistance > 0.0F && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
            if(canCrit) {
                damage *= 1.5;
            }
            damage += enchDamage;
            boolean damaged = bentity.updateHealth(-damage, Optional.of(player));
            if(damaged) {
                if(canCrit) {
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    Utilz.spawnCritParticles(world, pos);
                } else if(canSpecial && player.isSprinting()) {
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                } else {
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
                if(enchDamage > 0) {
                    Utilz.spawnEnchParticles(world, pos);
                }
                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            } else {
                world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HIT);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 15;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 15;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && !state.isOf(newState.getBlock())) {
            this.updateNeighbors(state, world, pos);
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        updateNeighbors(state, world, pos);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        for(Direction d : Direction.values()) {
            world.updateNeighborsAlways(pos.offset(d), this);
        }
    }

    static {
        HIT = BooleanProperty.of("hit");
    }
}
