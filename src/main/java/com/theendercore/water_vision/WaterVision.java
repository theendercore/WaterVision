package com.theendercore.water_vision;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterVision implements ClientModInitializer {
    public static final String MODID = "water_vision";
    public static final WaterVisionConfig config = ConfigApiJava.registerAndLoadConfig(WaterVisionConfig::new, RegisterType.CLIENT);
    public static final Logger log = LoggerFactory.getLogger(MODID);
    @Override
    public void onInitializeClient() {
        log.info("Water Blub Blub");
    }
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
