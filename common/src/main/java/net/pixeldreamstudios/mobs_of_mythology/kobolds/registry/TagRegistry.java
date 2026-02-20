package net.pixeldreamstudios.mobs_of_mythology.kobolds.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;

public record TagRegistry() {
    public static final TagKey<Block> KOBOLD_SPAWNABLE_ON =
        TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MythKobolds.MOD_ID, "kobold_spawnable_on"));

    public static TagKey<Biome> KOBOLD_BIOMES =
        TagKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MythKobolds.MOD_ID, "kobolds_spawn_in"));
}
