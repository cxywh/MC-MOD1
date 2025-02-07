package player.stevemod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class SteveEntity extends TamableAnimal implements NeutralMob {
    private Player owner;
    private boolean isDeadState = false;
    private static final Predicate<LivingEntity> TARGET_CONDITIONS = entity ->
            entity instanceof Mob && ((Mob) entity).getTarget() != null;

    public SteveEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.setTame(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 5.0F, 10.0F, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 10, true, false,
                entity -> TARGET_CONDITIONS.test(entity) &&
                        (entity.getLastHurtByMob() == this.owner || entity.getLastHurtByMob() == this)
        ));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<SteveEntity>(this, false));
    }

    @Override
    public UUID getOwnerUUID() {
        return owner != null ? owner.getUUID() : null;
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        if (uuid != null) {
            Player player = this.level().getPlayerByUUID(uuid);
            if (player != null) {
                this.owner = player;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && owner != null) {
            if (!isDeadState) {
                this.setHealth(owner.getHealth());
            }
            if (this.distanceTo(owner) > 10) {
                this.teleportTo(owner.getX(), owner.getY(), owner.getZ());
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            this.setTarget(attacker);
            if (this.owner != null) {
                this.owner.setLastHurtByMob(attacker);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (owner != null) {
            tag.putUUID("Owner", owner.getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Owner")) {
            UUID ownerUUID = tag.getUUID("Owner");
            this.setOwnerUUID(ownerUUID);
        }
    }

    public void setOwner(Player player) {
        this.owner = player;
        this.setTame(true);
    }

    public Player getOwner() {
        return owner;
    }

    public void revive() {
        this.setHealth(this.getMaxHealth());
        this.isDeadState = false;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }
}