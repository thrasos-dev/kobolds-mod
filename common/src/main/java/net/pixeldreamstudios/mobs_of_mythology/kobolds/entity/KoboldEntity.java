package net.pixeldreamstudios.mobs_of_mythology.kobolds.entity;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;
import org.jetbrains.annotations.Nullable;

public class KoboldEntity extends AbstractKoboldEntity {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK =
        SynchedEntityData.defineId(KoboldEntity.class, EntityDataSerializers.ITEM_STACK);

    public KoboldEntity(EntityType<? extends AbstractKoboldEntity> entityType, Level world) {
        super(entityType, world, Monster.XP_REWARD_SMALL);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, MythKobolds.config.koboldHealth)
            .add(Attributes.ATTACK_DAMAGE, MythKobolds.config.koboldAttackDamage)
            .add(Attributes.ATTACK_SPEED, 2)
            .add(Attributes.ATTACK_KNOCKBACK, 1)
            .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.2, 1.5) {
            @Override
            public boolean canUse() {
                return !KoboldEntity.this.getItemStack().isEmpty() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false) {
            @Override
            public boolean canUse() {
                return KoboldEntity.this.getItemStack().isEmpty() && super.canUse();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return KoboldEntity.this.getItemStack().isEmpty() && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (!getItemStack().isEmpty()) {
            nbt.put("ItemStack", this.getItemStack().save(this.registryAccess()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setItemStack(
            ItemStack.parse(this.registryAccess(), nbt.getCompound("ItemStack"))
                .orElse(ItemStack.EMPTY));
    }

    public ItemStack getItemStack() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public void setItemStack(ItemStack itemStack) {
        this.getEntityData().set(DATA_ITEM_STACK, itemStack);
        this.playSound(SoundEvents.VINDICATOR_CELEBRATE, 1.0f, 2.0f);
        this.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
    }

    @Override
    public SpawnGroupData finalizeSpawn(
        ServerLevelAccessor world,
        DifficultyInstance difficulty,
        MobSpawnType spawnReason,
        @Nullable SpawnGroupData entityData) {
        KoboldVariant variant = Util.getRandom(KoboldVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public void die(DamageSource arg) {
        super.die(arg);
        this.spawnAtLocation(getItemStack());
        setItemStack(ItemStack.EMPTY);
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean result = super.doHurtTarget(target);
        if (result) {
            this.triggerAnim("attackController", "attack");
            if (MythKobolds.config.shouldKoboldsSteal && getItemStack().isEmpty() && target instanceof LivingEntity livingTarget) {
                ItemStack targetItem = livingTarget.getItemInHand(InteractionHand.MAIN_HAND);
                if (!targetItem.isEmpty()) {
                    setItemStack(targetItem.copy());
                    targetItem.shrink(targetItem.getCount());
                    this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 255, false, false, false));
                    this.setTarget(null);
                }
            }
        }
        return result;
    }

    @Override
    public <T> T getVariant() {
        return (T) KoboldVariant.byId(this.getTypeVariant() & 255);
    }

    private void setVariant(KoboldVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
