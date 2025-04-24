package com.theendercore.water_vision.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.theendercore.water_vision.WaterVision.config;

@Debug(export = true)
@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = -8f, ordinal = 2))
    private static float modifyFogStartUnderwater(float constant) {
        return config().enable ? config().scale_close : constant;
    }

    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 96f))
    private static float modifyFogEndUnderwater(float constant,
                                                @Local(argsOnly = true, ordinal = 0) float viewDistance, @Local LocalRef<Entity> entity) {
        if (config().enable) {
            entity.set(null);
            return config().scale_far * viewDistance * 0.01f;
        }
        return constant;
    }

    @ModifyExpressionValue(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogParameters;fogEnd:F", opcode = Opcodes.GETFIELD, ordinal = 2))
    private static float stopFogOverride(float original, Camera camera, @Local LocalRef<Entity> entity) {
        if (config().enable) {
            entity.set(camera.getFocusedEntity());
            return 0f;
        }
        return original;
    }
}
