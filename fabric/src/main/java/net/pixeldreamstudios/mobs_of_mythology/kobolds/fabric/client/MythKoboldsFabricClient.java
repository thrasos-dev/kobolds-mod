package net.pixeldreamstudios.mobs_of_mythology.kobolds.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;

public final class MythKoboldsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MythKobolds.initClient();
    }
}
