package net.pixeldreamstudios.mobs_of_mythology.kobolds.entity;

import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.RawAnimation;

public final class DefaultAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    public static final RawAnimation ATTACK =
        RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE);
}
