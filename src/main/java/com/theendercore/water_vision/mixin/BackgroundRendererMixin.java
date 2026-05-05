package com.theendercore.water_vision.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.water_vision.WaterVision.config;

@Mixin({WaterFogEnvironment.class, LavaFogEnvironment.class})
public abstract class BackgroundRendererMixin {
    @Inject(method = "setupFog", at = @At("TAIL"))
    void modifyFog(FogData fogData, Camera camera, ClientLevel clientLevel, float viewDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        //noinspection ConstantValue
        if (!config.enableForLava &&  ((Object) this) instanceof LavaFogEnvironment) return;
        fogData.environmentalStart = config.scaleClose;
        fogData.environmentalEnd = config.scaleFar * viewDistance * 0.01f;

        fogData.skyEnd = fogData.environmentalEnd;
        fogData.cloudEnd = fogData.environmentalEnd;
    }
}
