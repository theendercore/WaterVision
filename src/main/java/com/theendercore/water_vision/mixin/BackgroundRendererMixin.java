package com.theendercore.water_vision.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.theendercore.water_vision.WaterVision.config;

@Debug(export = true)
@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @ModifyReturnValue(method = "applyFog", at = @At("RETURN"))
    private static FogParameters modifyFog(FogParameters original, Camera camera, @Local(argsOnly = true, ordinal = 0) float viewDistance) {
        if (original != FogParameters.NO_FOG && config.enable && camera.getSubmersionType() == CameraSubmersionType.WATER) {
            var entity = camera.getFocusedEntity();
            if (entity instanceof LivingEntity living && !(living.hasStatusEffect(StatusEffects.DARKNESS) || living.hasStatusEffect(StatusEffects.BLINDNESS))) {
                return new FogParameters(config.scaleClose, config.scaleFar * viewDistance * 0.01f,
                        original.shape(), original.red(), original.green(), original.blue(), original.alpha());
            }
        }
        return original;
    }
}
