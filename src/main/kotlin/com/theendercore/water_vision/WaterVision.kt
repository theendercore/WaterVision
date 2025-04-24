package com.theendercore.water_vision

import com.theendercore.water_vision.config.WaterVisionConfig
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object WaterVision {
    const val MODID = "water_vision"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(MODID)

    fun clientInit() {
        log.info("Water Blub Blub")
    }

    fun id(path: String): Identifier = Identifier.of(MODID, path)
    @JvmStatic
    fun config(): WaterVisionConfig = WaterVisionConfig.INSTANCE.instance()
}
