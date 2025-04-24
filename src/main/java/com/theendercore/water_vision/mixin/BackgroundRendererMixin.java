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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.water_vision.WaterVision.config;

@Debug(export = true)
@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = -8f, ordinal = 2))
    private static float modifyFogStartUnderwater(float constant) {
        return config().scale_close;
    }

    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 96f))
    private static float modifyFogEndUnderwater(float constant, @Local(argsOnly = true, ordinal = 0) float viewDistance) {
        return config().scale_far * viewDistance * 0.01f;
    }
    
    @Inject(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogParameters;fogEnd:F", opcode = Opcodes.PUTFIELD, ordinal = 5, shift = At.Shift.AFTER))
    private static void turnOffExtraChecks(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta,
                                           CallbackInfo ci, @Local LocalRef<Entity> entity) {
        entity.set(null);
    }

    @ModifyExpressionValue(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogParameters;fogEnd:F", opcode = Opcodes.GETFIELD, ordinal = 2))
    private static float stopFogOverride(float original, Camera camera, @Local LocalRef<Entity> entity) {
        entity.set(camera.getFocusedEntity());
        return 0f;
    }
}
