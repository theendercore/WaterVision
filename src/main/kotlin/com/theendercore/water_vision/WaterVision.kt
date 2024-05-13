package com.theendercore.water_vision

import com.theendercore.water_vision.config.WaterVisionConfig
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object WaterVision {
    const val MODID = "water_vision"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MODID)

    fun clientInit() {
        LOGGER.info("Water Blub Blub")
    }

    fun id(path: String): Identifier = Identifier(MODID, path)
    @JvmStatic
    fun config(): WaterVisionConfig = WaterVisionConfig.INSTANCE.instance()
}
