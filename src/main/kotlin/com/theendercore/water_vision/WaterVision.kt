package com.theendercore.water_vision

import com.theendercore.water_vision.config.WaterVisionConfig
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MODID = "water_vision"

@JvmField
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@Suppress("unused")
fun id(path: String): Identifier = Identifier(MODID, path)



@Suppress("unused")
fun onInitialize() {
    LOGGER.info("Water Blub Blub")
}

fun config(): WaterVisionConfig = WaterVisionConfig.INSTANCE.config
