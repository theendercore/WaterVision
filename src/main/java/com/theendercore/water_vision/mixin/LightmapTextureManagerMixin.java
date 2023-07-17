package com.theendercore.water_vision.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.dimension.DimensionType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.water_vision.WaterVisionKt.config;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Final
    @Shadow
    private MinecraftClient client;
    @Shadow
    private boolean dirty;
    @Final
    @Shadow
    private GameRenderer renderer;
    @Final
    @Shadow
    private NativeImageBackedTexture texture;
    @Final
    @Shadow
    private NativeImage image;


    @Inject(at = @At("HEAD"), method = "update", cancellable = true)
    public void update(float delta, CallbackInfo ci) {
        if (this.dirty && config().enable) {
            ClientWorld clientWorld = this.client.world;
            ClientPlayerEntity player = this.client.player;
            if (clientWorld != null && player != null && player.isSubmergedInWater()) {
                this.dirty = false;
                float f = clientWorld.getSkyBrightness(1.0F);
                float g;
                if (clientWorld.getLightningTicksLeft() > 0) g = 1.0F;
                else g = f * 0.95F + 0.05F;


                float h = (this.client.options.getDarknessEffectScale().getValue()).floatValue();
                float i = this.getDarknessFactor(delta) * h;
                float j = this.getDarkness(this.client.player, i, delta) * h;
                float l = 1f;


                Vector3f vector3f = new Vector3f(f, f, 1.0F).lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
                Vector3f vector3f2 = new Vector3f();

                for (int n = 0; n < 16; ++n) {
                    for (int o = 0; o < 16; ++o) {
                        float p = getBrightness(clientWorld.getDimension(), n) * g;
                        float q = getBrightness(clientWorld.getDimension(), o) * 1.5f;
                        float s = q * ((q * 0.6F + 0.4F) * 0.6F + 0.4F);
                        float t = q * (q * q * 0.6F + 0.4F);
                        vector3f2.set(q, s, t);
                        boolean bl = clientWorld.getDimensionEffects().shouldBrightenLighting();
                        if (bl) {
                            vector3f2.lerp(new Vector3f(0.99F, 1.12F, 1.0F), 0.25F);
                            clamp(vector3f2);
                        } else {
                            Vector3f vector3f3 = new Vector3f(vector3f).mul(p);
                            vector3f2.add(vector3f3);
                            vector3f2.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                            if (this.renderer.getSkyDarkness(delta) > 0.0F) {
                                float u = this.renderer.getSkyDarkness(delta);
                                Vector3f vector3f4 = new Vector3f(vector3f2).mul(0.7F, 0.6F, 0.6F);
                                vector3f2.lerp(vector3f4, u);
                            }
                        }


                        float qz = Math.max(vector3f2.x(), Math.max(vector3f2.y(), vector3f2.z()));
                        if (qz < 1.0F) {
                            float u = 1.0F / qz;
                            Vector3f vector3f4 = new Vector3f(vector3f2).mul(u);
                            vector3f2.lerp(vector3f4, l);
                        }


                        if (!bl) {
                            if (j > 0.0F) {
                                vector3f2.add(-j, -j, -j);
                            }
                            clamp(vector3f2);
                        }

                        float v = (this.client.options.getGamma().getValue()).floatValue();
                        Vector3f vector3f5 = new Vector3f(this.easeOutQuart(vector3f2.x), this.easeOutQuart(vector3f2.y), this.easeOutQuart(vector3f2.z));
                        vector3f2.lerp(vector3f5, Math.max(0.0F, v - i));
                        vector3f2.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                        clamp(vector3f2);
                        vector3f2.mul(255.0F);
                        int x = (int) vector3f2.x();
                        int y = (int) vector3f2.y();
                        int z = (int) vector3f2.z();
                        assert this.image != null;
                        this.image.setColor(o, n, 0xFF000000 | z << 16 | y << 8 | x);
                    }
                }
                this.texture.upload();
                ci.cancel();
            }
        }

    }

    @Shadow
    private static void clamp(Vector3f vec) {
    }

    @Shadow
    private float easeOutQuart(float val) {
        return 0f;
    }

    @Shadow
    public static float getBrightness(DimensionType type, int lightLevel) {
        return 0f;
    }

    @Shadow
    private float getDarknessFactor(float delta) {
        return 0f;
    }

    @Shadow
    private float getDarkness(LivingEntity entity, float factor, float delta) {
        return 0f;
    }

}
