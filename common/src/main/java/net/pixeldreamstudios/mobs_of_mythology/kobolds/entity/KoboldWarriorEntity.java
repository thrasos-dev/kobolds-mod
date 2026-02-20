package net.pixeldreamstudios.mobs_of_mythology.kobolds.entity;

import net.minecraft.Util;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;

public class KoboldWarriorEntity extends AbstractKoboldEntity {
    public KoboldWarriorEntity(EntityType<? extends AbstractKoboldEntity> entityType, Level world) {
        super(entityType, world, Monster.XP_REWARD_MEDIUM);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.KOBOLD_SPEAR.get(), 1));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, MythKobolds.config.koboldWarriorHealth)
            .add(Attributes.ARMOR, MythKobolds.config.koboldWarriorArmor)
            .add(Attributes.ATTACK_DAMAGE, MythKobolds.config.koboldWarriorAttackDamage)
            .add(Attributes.ATTACK_SPEED, 2)
            .add(Attributes.ATTACK_KNOCKBACK, 1)
            .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean result = super.doHurtTarget(target);
        if (result) {
            this.triggerAnim("attackController", "attack");
        }
        return result;
    }

    @Override
    public SpawnGroupData finalizeSpawn(
        ServerLevelAccessor world,
        DifficultyInstance difficulty,
        MobSpawnType spawnReason,
        @Nullable SpawnGroupData entityData) {
        KoboldWarriorVariant variant = Util.getRandom(KoboldWarriorVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public <T> T getVariant() {
        return (T) KoboldWarriorVariant.byId(this.getTypeVariant() & 255);
    }

    private void setVariant(KoboldWarriorVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
