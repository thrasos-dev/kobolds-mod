package net.pixeldreamstudios.mobs_of_mythology.kobolds.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(MythKobolds.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> MYTH_KOBOLDS_TAB =
        TABS.register(
            "myth_kobolds_tab",
            () ->
                CreativeTabRegistry.create(
                    Component.translatable("category.myth_kobolds"),
                    () -> new ItemStack(ItemRegistry.KOBOLD_SPEAR.get())));

    public static void init() {
        TABS.register();
    }
}
