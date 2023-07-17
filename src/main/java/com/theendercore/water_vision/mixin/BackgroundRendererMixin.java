package com.theendercore.water_vision.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.theendercore.water_vision.ConfigData;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.water_vision.WaterVisionKt.config;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(at = @At("TAIL"), method = "applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZF)V")
    private static void applyFog(Camera camera, FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        final ConfigData c = config().getConfig();
        CameraSubmersionType fogType2 = camera.getSubmersionType();
        boolean hasEffects = (camera.getFocusedEntity() instanceof LivingEntity mob)
                && (mob.hasStatusEffect(StatusEffects.BLINDNESS) || (mob.hasStatusEffect(StatusEffects.DARKNESS)));
        if (fogType2 == CameraSubmersionType.WATER && !hasEffects && c.enable) {
            RenderSystem.setShaderFogStart(c.scale_close);
            RenderSystem.setShaderFogEnd(c.scale_far);
        }
    }
}
