package net.pixeldreamstudios.mobs_of_mythology.kobolds.fabric;

import net.fabricmc.api.ModInitializer;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;

public final class MythKoboldsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MythKobolds.init();
    }
}
