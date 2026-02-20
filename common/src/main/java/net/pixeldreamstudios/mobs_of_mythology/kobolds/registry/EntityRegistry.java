package net.pixeldreamstudios.mobs_of_mythology.kobolds.registry;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.entity.AbstractKoboldEntity;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.entity.KoboldEntity;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.entity.KoboldWarriorEntity;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(MythKobolds.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<KoboldEntity>> KOBOLD =
        ENTITIES.register(
            "kobold",
            () ->
                EntityType.Builder.of(KoboldEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.75f)
                    .build(
                        ResourceLocation.fromNamespaceAndPath(MythKobolds.MOD_ID, "kobold")
                            .toString()));

    public static final RegistrySupplier<EntityType<KoboldWarriorEntity>> KOBOLD_WARRIOR =
        ENTITIES.register(
            "kobold_warrior",
            () ->
                EntityType.Builder.of(KoboldWarriorEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.75f)
                    .build(
                        ResourceLocation.fromNamespaceAndPath(MythKobolds.MOD_ID, "kobold_warrior")
                            .toString()));

    private static void initSpawns() {
        SpawnPlacementsRegistry.register(
            EntityRegistry.KOBOLD,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractKoboldEntity::checkKoboldSpawnRules);
        BiomeModifications.addProperties(
            b -> b.hasTag(TagRegistry.KOBOLD_BIOMES),
            (ctx, b) ->
                b.getSpawnProperties()
                    .addSpawn(
                        MobCategory.MONSTER,
                        new MobSpawnSettings.SpawnerData(
                            KOBOLD.get(), MythKobolds.config.koboldSpawnWeight, 2, 4)));

        SpawnPlacementsRegistry.register(
            EntityRegistry.KOBOLD_WARRIOR,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractKoboldEntity::checkKoboldSpawnRules);
        BiomeModifications.addProperties(
            b -> b.hasTag(TagRegistry.KOBOLD_BIOMES),
            (ctx, b) ->
                b.getSpawnProperties()
                    .addSpawn(
                        MobCategory.MONSTER,
                        new MobSpawnSettings.SpawnerData(
                            KOBOLD_WARRIOR.get(),
                            MythKobolds.config.koboldWarriorSpawnWeight,
                            2,
                            3)));
    }

    private static void initAttributes() {
        EntityAttributeRegistry.register(KOBOLD, KoboldEntity::createAttributes);
        EntityAttributeRegistry.register(KOBOLD_WARRIOR, KoboldWarriorEntity::createAttributes);
    }

    public static void init() {
        ENTITIES.register();
        initAttributes();
        initSpawns();
    }
}
