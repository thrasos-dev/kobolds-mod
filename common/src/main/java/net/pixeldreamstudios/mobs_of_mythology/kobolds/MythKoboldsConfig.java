package net.pixeldreamstudios.mobs_of_mythology.kobolds;

import mod.azure.azurelib.common.api.common.config.Config;
import mod.azure.azurelib.common.internal.common.config.Configurable;

@Config(id = MythKobolds.MOD_ID)
public class MythKoboldsConfig {
    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double koboldHealth = 10.0;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double koboldAttackDamage = 1.5;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1.0f)
    public float koboldFleeSpeedMod = 2.0f;

    @Configurable @Configurable.Synchronized public int koboldSpawnWeight = 10;
    @Configurable @Configurable.Synchronized public boolean shouldKoboldsSteal = true;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double koboldWarriorHealth = 20.0;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double koboldWarriorArmor = 6.0;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double koboldWarriorAttackDamage = 5.5;

    @Configurable @Configurable.Synchronized public int koboldWarriorSpawnWeight = 10;
}
