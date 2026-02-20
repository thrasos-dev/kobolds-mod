package net.pixeldreamstudios.mobs_of_mythology.kobolds;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.internal.common.AzureLibMod;
import mod.azure.azurelib.common.internal.common.config.format.ConfigFormats;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.client.KoboldRenderer;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.client.KoboldWarriorRenderer;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MythKobolds {
    public static final String MOD_ID = "myth_kobolds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MythKoboldsConfig config;

    public static void init() {
        AzureLib.initialize();
        config = AzureLibMod.registerConfig(MythKoboldsConfig.class, ConfigFormats.properties()).getConfigInstance();
        EntityRegistry.init();
        ItemRegistry.init();
        TabRegistry.init();
    }

    public static void initClient() {
        EntityRendererRegistry.register(EntityRegistry.KOBOLD, KoboldRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.KOBOLD_WARRIOR, KoboldWarriorRenderer::new);
    }
}
