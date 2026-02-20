package net.pixeldreamstudios.mobs_of_mythology.kobolds.registry;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(MythKobolds.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> KOBOLD_SPAWN_EGG =
        ITEMS.register(
            "kobold_spawn_egg",
            () ->
                new ArchitecturySpawnEggItem(
                    EntityRegistry.KOBOLD,
                    0xd5f07d,
                    0x637036,
                    new Item.Properties().arch$tab(TabRegistry.MYTH_KOBOLDS_TAB)));

    public static final RegistrySupplier<Item> KOBOLD_WARRIOR_SPAWN_EGG =
        ITEMS.register(
            "kobold_warrior_spawn_egg",
            () ->
                new ArchitecturySpawnEggItem(
                    EntityRegistry.KOBOLD_WARRIOR,
                    0xd5f07d,
                    0xe6b800,
                    new Item.Properties().arch$tab(TabRegistry.MYTH_KOBOLDS_TAB)));

    public static final RegistrySupplier<Item> KOBOLD_SPEAR =
        ITEMS.register(
            "kobold_spear",
            () ->
                new SwordItem(
                    Tiers.IRON,
                    new Item.Properties()
                        .attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.0F))
                        .rarity(Rarity.UNCOMMON)
                        .arch$tab(TabRegistry.MYTH_KOBOLDS_TAB)));

    public static void init() {
        ITEMS.register();
    }
}
