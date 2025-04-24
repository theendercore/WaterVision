package com.theendercore.water_vision

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.api.RegisterType
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object WaterVision {
    const val MODID = "water_vision"
    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::WaterVisionConfig, RegisterType.CLIENT)
    @JvmField
    val log: Logger = LoggerFactory.getLogger(MODID)

    fun clientInit() {
        log.info("Water Blub Blub")
    }

    fun id(path: String): Identifier = Identifier.of(MODID, path)
}
