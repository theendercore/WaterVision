package com.theendercore.water_vision

import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber.Companion.withIncrement
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber.WidgetType

class WaterVisionConfig : Config(WaterVision.id(WaterVision.MODID)) {

    @JvmField
    var enable = true

    @Suppress("unused")
    var fogGroup = ConfigGroup("fog", false)

    @JvmField
    var scaleClose = 25f

    @JvmField
    @ConfigGroup.Pop
    var scaleFar = 165f

    @Suppress("unused")
    var transitionGroup = ConfigGroup("transition", false)

    @JvmField
    var enableTransition = true

    @JvmField
    var transitionMultiplier = ValidatedFloat(0.5f, 100f, 0.01f, WidgetType.TEXTBOX_WITH_BUTTONS).withIncrement(0.1f)

    @JvmField
    @ConfigGroup.Pop
    var transitionLeaveMultiplier = ValidatedInt(3, 32, 1, WidgetType.TEXTBOX_WITH_BUTTONS).withIncrement(1)
}