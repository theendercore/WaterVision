package com.theendercore.water_vision.config

import com.theendercore.water_vision.LOGGER
import com.theendercore.water_vision.MODID
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.config.ConfigEntry
import dev.isxander.yacl3.config.ConfigInstance
import dev.isxander.yacl3.config.GsonConfigInstance
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text


class WaterVisionConfig() {


    @ConfigEntry
    @JvmField
    var enable = true

    @ConfigEntry
    @JvmField
    var scale_close = 25f

    @ConfigEntry
    @JvmField
    var scale_far = 200f

    companion object {
        val INSTANCE: ConfigInstance<WaterVisionConfig> = GsonConfigInstance
            .createBuilder(WaterVisionConfig::class.java)
            .setPath(FabricLoader.getInstance().configDir.resolve("$MODID.json"))
            .build()

        fun makeScreen(parent: Screen?): Screen {
            LOGGER.info("Make screen")
            return YetAnotherConfigLib.create(INSTANCE)
            { defaults, config, builder ->
                builder
                    .title(genText("title"))
                    .category(
                        ConfigCategory.createBuilder()
                            .name(genText("category"))
                            .options(
                                listOf(
                                    Option.createBuilder<Boolean>()
                                        .name(genText("enable"))
                                        .binding(defaults.enable, { config.enable }, { config.enable = it })
                                        .controller { TickBoxControllerBuilder.create(it) }
                                        .build(),
                                    Option.createBuilder<Float>()
                                        .name(genText("scale_close"))
                                        .binding(
                                            defaults.scale_close,
                                            { config.scale_close },
                                            { config.scale_close = it })
                                        .controller { FloatFieldControllerBuilder.create(it) }
                                        .build(),
                                    Option.createBuilder<Float>()
                                        .name(genText("scale_far"))
                                        .binding(
                                            defaults.scale_far,
                                            { config.scale_far },
                                            { config.scale_far = it })
                                        .controller { FloatFieldControllerBuilder.create(it) }
                                        .build(),
                                )
                            )
                            .build()
                    )
            }.generateScreen(parent)
        }

        private fun genText(text: String): Text {
            return Text.translatable("config.water_vision.$text")
        }
    }
}
