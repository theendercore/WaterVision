package com.theendercore.water_vision

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
    LOGGER.info(MODID)
    config().load()
    Keybinding.init()
}

fun config(): Config {
    return Config.INSTANCE
}