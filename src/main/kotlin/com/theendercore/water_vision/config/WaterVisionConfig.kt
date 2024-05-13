package com.theendercore.water_vision.config

import com.theendercore.water_vision.WaterVision.MODID
import com.theendercore.water_vision.WaterVision.id
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

@Suppress("VariableNaming", "PropertyName", "MagicNumber")
class WaterVisionConfig {
    @SerialEntry
    @JvmField
    var enable = true

    @SerialEntry
    @JvmField
    var scale_close = 25f

    @SerialEntry
    @JvmField
    var scale_far = 200f

    @SerialEntry
    @JvmField
    var enableTransition = true

    @SerialEntry
    @JvmField
    var transitionMultiplier = 0.5f

    @SerialEntry
    @JvmField
    var transitionLeaveMultiplier = 3


    companion object {
        val cfgPath = FabricLoader.getInstance().configDir.resolve("$MODID.json")

        val INSTANCE: ConfigClassHandler<WaterVisionConfig> =
            ConfigClassHandler.createBuilder(WaterVisionConfig::class.java)
                .id(id(MODID))
                .serializer {
                    GsonConfigSerializerBuilder.create(it)
                        .setPath(cfgPath)
                        .build()
                }
                .build()

        fun makeScreen(parent: Screen?): Screen {
            return YetAnotherConfigLib.create(INSTANCE) { defaults, config, builder ->
                builder
                    .title(tTxt("title"))
                    .category(
                        ConfigCategory.createBuilder()
                            .name(tTxt("category"))
                            .options(
                                listOf(
                                    option<Boolean>("enable")
                                        .binding(defaults.enable, { config.enable }, { config.enable = it })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build(),

                                    option<Float>("scale.close")
                                        .binding(
                                            defaults.scale_close,
                                            { config.scale_close },
                                            { config.scale_close = it }
                                        )
                                        .controller(FloatFieldControllerBuilder::create)
                                        .build(),

                                    option<Float>("scale.far")
                                        .binding(defaults.scale_far,
                                            { config.scale_far },
                                            { config.scale_far = it }
                                        )
                                        .controller(FloatFieldControllerBuilder::create)
                                        .build(),

                                    option<Boolean>("transition.enable")
                                        .description(OptionDescription.of(tTxt("transition.enable.description")))
                                        .binding(
                                            defaults.enableTransition,
                                            { config.enableTransition },
                                            { config.enableTransition = it })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build(),

                                    option<Float>("transition.multiplier")
                                        .description(OptionDescription.of(tTxt("transition.multiplier.description")))
                                        .binding(defaults.transitionMultiplier,
                                            { config.transitionMultiplier },
                                            { config.transitionMultiplier = it }
                                        )
                                        .controller { FloatFieldControllerBuilder.create(it).min(0.01f).max(100f) }
                                        .build(),

                                    option<Int>("transition.leave.multiplier")
                                        .description(OptionDescription.of(tTxt("transition.leave.multiplier.description")))
                                        .binding(defaults.transitionLeaveMultiplier,
                                            { config.transitionLeaveMultiplier },
                                            { config.transitionLeaveMultiplier = it }
                                        )
                                        .controller { IntegerFieldControllerBuilder.create(it).min(1).max(32) }
                                        .build(),

                                )
                            )
                            .build()
                    )
            }.generateScreen(parent)
        }

        private fun <T> option(name: String): Option.Builder<T> = Option.createBuilder<T>().name(tTxt(name))
        private fun tTxt(text: String): Text = Text.translatable("config.water_vision.$text")
    }
}
