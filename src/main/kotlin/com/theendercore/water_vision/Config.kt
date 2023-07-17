package com.theendercore.water_vision

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException


class Config {
    private val defaultConfig = ConfigData(true, 25f, 100f)
    private val configFile: File = FabricLoader.getInstance().configDir.resolve("$MODID.json").toFile()

    var config: ConfigData = defaultConfig

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json { prettyPrint = true; prettyPrintIndent = "\t" }

    fun load() {
        LOGGER.info(configFile.path)
        try {
            val stringData = FileReader(configFile).use { it.readText() }
            config = json.decodeFromString(stringData)
        } catch (e: IOException) {
            LOGGER.info("No config Found! Making a new one.")
            save(defaultConfig)
        }
    }

    private fun save(config: ConfigData) = FileWriter(configFile).use { it.write(json.encodeToString(config)) }

    companion object {
        val INSTANCE = Config()
    }
}


@Serializable
data class ConfigData(
    @JvmField val enable: Boolean,
    @JvmField val scale_close: Float,
    @JvmField val scale_far: Float,
)