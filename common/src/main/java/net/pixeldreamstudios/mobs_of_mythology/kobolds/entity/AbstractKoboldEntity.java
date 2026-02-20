package net.pixeldreamstudios.mobs_of_mythology.kobolds.entity;

import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animatable.instance.SingletonAnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.registry.TagRegistry;

public abstract class AbstractKoboldEntity extends Monster implements GeoEntity, Enemy {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
        SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.INT);

    protected AbstractKoboldEntity(EntityType<? extends Monster> entityType, Level level, int XP) {
        super(entityType, level);
        this.xpReward = XP;
    }

    public static boolean checkKoboldSpawnRules(
        EntityType<? extends Monster> entityType,
        LevelAccessor levelAccessor,
        MobSpawnType mobSpawnType,
        BlockPos blockPos,
        RandomSource randomSource) {
        boolean bl =
            levelAccessor.getDifficulty() != Difficulty.PEACEFUL
                && checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
        return levelAccessor.getBlockState(blockPos.below()).is(TagRegistry.KOBOLD_SPAWNABLE_ON)
            && bl;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar
            .add(
                new AnimationController<>(
                    this,
                    "livingController",
                    3,
                    state -> {
                        if (state.isMoving() && !swinging) {
                            if (isAggressive() && !swinging) {
                                state.getController().setAnimation(DefaultAnimations.RUN);
                                return PlayState.CONTINUE;
                            }
                            state.getController().setAnimation(DefaultAnimations.WALK);
                            return PlayState.CONTINUE;
                        }
                        state.getController().setAnimation(DefaultAnimations.IDLE);
                        return PlayState.CONTINUE;
                    }))
            .add(
                new AnimationController<>(
                        this,
                        "attackController",
                        3,
                        event -> {
                            swinging = false;
                            return PlayState.STOP;
                        })
                    .triggerableAnim("attack", DefaultAnimations.ATTACK));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }

    protected int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    protected abstract <T> T getVariant();

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        this.playSound(SoundEvents.VINDICATOR_AMBIENT, 1.0f, 1.75f);
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        this.playSound(SoundEvents.VINDICATOR_HURT, 1.0f, 1.75f);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.VINDICATOR_DEATH, 1.0f, 1.75f);
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 1.0f, 1.75f);
    }
}
