package com.theendercore.water_vision.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.theendercore.water_vision.WaterVision.config;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Final
    @Shadow
    private MinecraftClient client;
    @Unique
    int waterTicks = 0;

    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 6)
    private float updateLightning(float initValue, @Local(ordinal = 0, argsOnly = true) float ticksDelta) {
        if (config().enable && client.player != null && !hasEffects()) {
            boolean isSubmerged = client.player.isSubmergedInWater();
            if (config().enableTransition) {
                float scale = (config().transitionMultiplier * 100);
                if (isSubmerged && waterTicks < scale) waterTicks++;
                else if (!isSubmerged && waterTicks > 0)
                    waterTicks -= Math.min(waterTicks, config().transitionLeaveMultiplier);

//                client.player.sendMessage(Text.of("Value : " + waterTicks + "/" + scale), true);
                return Math.min(waterTicks / scale, 1f);
            } else if (isSubmerged) return 1f;
        }
        return initValue;
    }

    @Unique
    private boolean hasEffects() {
        assert client.player != null;
        return client.player.hasStatusEffect(StatusEffects.NIGHT_VISION) || client.player.hasStatusEffect(StatusEffects.CONDUIT_POWER);
    }
}
