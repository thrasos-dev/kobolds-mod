package net.pixeldreamstudios.mobs_of_mythology.kobolds.neoforge;

import dev.architectury.utils.EnvExecutor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;

@Mod(MythKobolds.MOD_ID)
public final class MythKoboldsNeoForge {
    public MythKoboldsNeoForge() {
        MythKobolds.init();
        EnvExecutor.runInEnv(Dist.CLIENT, () -> MythKobolds::initClient);
    }
}
