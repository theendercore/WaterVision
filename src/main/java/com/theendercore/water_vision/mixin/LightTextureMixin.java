package com.theendercore.water_vision.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.theendercore.water_vision.WaterVision.config;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @Final
    @Shadow
    private Minecraft minecraft;
    @Unique
    int water_vision$waterTicks = 0;

    @ModifyVariable(method = "updateLightTexture", at = @At("STORE"), ordinal = 6)
    private float updateLightning(float initValue, @Local(ordinal = 0, argsOnly = true) float ticksDelta) {
        if (config.enable && minecraft.player != null && !hasEffects()) {
            boolean isSubmerged = minecraft.player.isUnderWater();
            if (config.enableTransition) {
                float scale = (config.transitionMultiplier.get() * 100);
                if (isSubmerged && water_vision$waterTicks < scale) water_vision$waterTicks++;
                else if (!isSubmerged && water_vision$waterTicks > 0)
                    water_vision$waterTicks -= Math.min(water_vision$waterTicks, config.transitionLeaveMultiplier.get());
                return Math.min(water_vision$waterTicks / scale, 1f);
            } else if (isSubmerged) return 1f;
        }
        return initValue;
    }

    @Unique
    private boolean hasEffects() {
        assert minecraft.player != null;
        return minecraft.player.hasEffect(MobEffects.NIGHT_VISION) || minecraft.player.hasEffect(MobEffects.CONDUIT_POWER);
    }
}
