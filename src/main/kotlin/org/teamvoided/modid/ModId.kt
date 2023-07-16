package org.teamvoided.modid


import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory


const val MODID = "modid"

@JvmField
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@Suppress("unused")
fun id(path: String): Identifier = Identifier(MODID, path)


@Suppress("unused")
fun onInitialize() {
    LOGGER.info(MODID)
}