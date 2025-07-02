package com.theendercore.water_vision.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.water_vision.WaterVision.config;

@Mixin(WaterFogEnvironment.class)
public abstract class BackgroundRendererMixin {
    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void modifyFog(FogData fogData, Entity entity, BlockPos blockPos, ClientLevel clientLevel, float f, DeltaTracker deltaTracker, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) float viewDistance) {
        fogData.environmentalStart  = config.scaleClose;
        fogData.environmentalEnd  = config.scaleFar * viewDistance * 0.01f;

        fogData.skyEnd = fogData.environmentalEnd;
        fogData.cloudEnd = fogData.environmentalEnd;
    }
}
