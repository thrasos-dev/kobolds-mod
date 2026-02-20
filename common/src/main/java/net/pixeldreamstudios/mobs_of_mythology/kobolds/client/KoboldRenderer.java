package net.pixeldreamstudios.mobs_of_mythology.kobolds.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import mod.azure.azurelib.common.api.client.renderer.DynamicGeoEntityRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.BlockAndItemGeoLayer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.MythKobolds;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.entity.KoboldEntity;
import net.pixeldreamstudios.mobs_of_mythology.kobolds.entity.KoboldVariant;
import org.jetbrains.annotations.Nullable;

public class KoboldRenderer extends DynamicGeoEntityRenderer<KoboldEntity> {
    public static final Map<KoboldVariant, ResourceLocation> LOCATION_BY_VARIANT =
        Util.make(
            Maps.newEnumMap(KoboldVariant.class),
            (map) -> {
                map.put(
                    KoboldVariant.KOBOLD,
                    ResourceLocation.fromNamespaceAndPath(
                        MythKobolds.MOD_ID, "textures/entity/kobold/kobold.png"));
                map.put(
                    KoboldVariant.KOBOLD_CLOTHED,
                    ResourceLocation.fromNamespaceAndPath(
                        MythKobolds.MOD_ID, "textures/entity/kobold/kobold_cloth.png"));
            });
    private static final String RIGHT_HAND = "hand";
    private static final String LEFT_HAND = "hand2";
    protected ItemStack mainHandItem;
    protected ItemStack offHandItem;

    public KoboldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new KoboldModel());
        this.shadowRadius = 0.4f;

        addRenderLayer(
            new BlockAndItemGeoLayer<>(this) {
                @Nullable
                @Override
                protected ItemStack getStackForBone(GeoBone bone, KoboldEntity animatable) {
                    return switch (bone.getName()) {
                        case LEFT_HAND ->
                            animatable.isLeftHanded()
                                ? KoboldRenderer.this.mainHandItem
                                : KoboldRenderer.this.offHandItem;
                        case RIGHT_HAND ->
                            animatable.isLeftHanded()
                                ? KoboldRenderer.this.offHandItem
                                : KoboldRenderer.this.mainHandItem;
                        default -> null;
                    };
                }

                @Override
                protected ItemDisplayContext getTransformTypeForStack(
                    GeoBone bone, ItemStack stack, KoboldEntity animatable) {
                    return switch (bone.getName()) {
                        case LEFT_HAND, RIGHT_HAND -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                        default -> ItemDisplayContext.NONE;
                    };
                }

                @Override
                protected void renderStackForBone(
                    PoseStack poseStack,
                    GeoBone bone,
                    ItemStack stack,
                    KoboldEntity animatable,
                    MultiBufferSource bufferSource,
                    float partialTick,
                    int packedLight,
                    int packedOverlay) {
                    if (stack == KoboldRenderer.this.mainHandItem) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
                        if (stack.getItem() instanceof ShieldItem) poseStack.translate(0, 0.125, -0.25);
                    } else if (stack == KoboldRenderer.this.offHandItem) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
                        if (stack.getItem() instanceof ShieldItem) {
                            poseStack.translate(0, 0.125, 0.25);
                            poseStack.mulPose(Axis.YP.rotationDegrees(180));
                        }
                    }
                    super.renderStackForBone(
                        poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
                }
            });
    }

    @Override
    public ResourceLocation getTextureLocation(KoboldEntity animatable) {
        return LOCATION_BY_VARIANT.get(animatable.getVariant());
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        KoboldEntity animatable,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        int colour) {
        super.preRender(
            poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        this.mainHandItem = animatable.getMainHandItem();
        this.offHandItem = animatable.getOffhandItem();
    }
}
