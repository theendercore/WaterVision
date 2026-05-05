package com.theendercore.water_vision.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.water_vision.WaterVision.config;

@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorMixin {
    @Final
    @Shadow
    private Minecraft minecraft;
    @Unique
    int water_vision$waterTicks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    void addTicking(CallbackInfo ci) {
        if (!config.enableTransition || minecraft.player == null) return;

        boolean isSubmerged = minecraft.player.isUnderWater();
        float scale = (config.transitionMultiplier.get() * 100);

        if (isSubmerged && water_vision$waterTicks < scale) {
            water_vision$waterTicks++;
        } else if (!isSubmerged && water_vision$waterTicks > 0) {
            water_vision$waterTicks -= Math.min(water_vision$waterTicks, config.transitionLeaveMultiplier.get());
        }
    }

    @Inject(method = "extract", at = @At("TAIL"))
    private void modifyState(LightmapRenderState state, float partialTicks, CallbackInfo ci) {
        if (config.enable && minecraft.player != null && !hasEffects()) {
            Float lightOverride = null;
            if (config.enableTransition) {
                float scale = (config.transitionMultiplier.get() * 100);
                lightOverride = Math.min(water_vision$waterTicks / scale, 1f);
            } else if (minecraft.player.isUnderWater()) {
                lightOverride = 1f;
            }
            if (lightOverride != null) {
                state.nightVisionEffectIntensity = lightOverride;
                if (config.tintUnderwaterLight) {
                    state.nightVisionColor = config.getTintColor();
                }
            }
        }
    }

    @Unique
    private boolean hasEffects() {
        assert minecraft.player != null;
        return minecraft.player.hasEffect(MobEffects.NIGHT_VISION) || minecraft.player.hasEffect(MobEffects.CONDUIT_POWER);
    }
}
