package com.theendercore.water_vision.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.theendercore.water_vision.WaterVision.config;

@Mixin(FogRenderer.class)
public abstract class BackgroundRendererMixin {
    @ModifyReturnValue(method = "setupFog", at = @At("RETURN"))
    private static FogParameters modifyFog(FogParameters original, Camera camera, @Local(argsOnly = true, ordinal = 0) float viewDistance) {
        if (original != FogParameters.NO_FOG && config.enable && camera.getFluidInCamera() == FogType.WATER) {
            var entity = camera.getEntity();
            if (entity instanceof LivingEntity living && !(living.hasEffect(MobEffects.DARKNESS) || living.hasEffect(MobEffects.BLINDNESS))) {
                return new FogParameters(config.scaleClose, config.scaleFar * viewDistance * 0.01f,
                        original.shape(), original.red(), original.green(), original.blue(), original.alpha());
            }
        }
        return original;
    }
}
