package com.theendercore.water_vision;

import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber.WidgetType;
import org.joml.Vector3f;

import java.awt.*;

import static me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber.Companion;


public class WaterVisionConfig extends Config {
    public WaterVisionConfig() {
        super(WaterVision.id(WaterVision.MODID));
    }

    public boolean enable = true;

    @SuppressWarnings("unused")
    ConfigGroup fogGroup = new ConfigGroup("fog", false);

    public float scaleClose = 25f;

    @ConfigGroup.Pop
    public float scaleFar = 165f;

    @SuppressWarnings("unused")
    ConfigGroup transitionGroup = new ConfigGroup("transition", false);

    public boolean enableTransition = true;

    public ValidatedFloat transitionMultiplier = Companion.withIncrement(new ValidatedFloat(0.5f, 100f, 0.01f, WidgetType.TEXTBOX_WITH_BUTTONS), 0.1f);

    @ConfigGroup.Pop
    public ValidatedInt transitionLeaveMultiplier = Companion.withIncrement(new ValidatedInt(3, 32, 1, WidgetType.TEXTBOX_WITH_BUTTONS), 1);

    public boolean enableForLava = true;

    public boolean tintUnderwaterLight = true;

    public ValidatedColor underwaterLightTint = new ValidatedColor(new Color(159, 181, 234), false);

    public Vector3f getTintColor() {
        return new Vector3f(
                underwaterLightTint.r() / 255f,
                underwaterLightTint.g() / 255f,
                underwaterLightTint.b() / 255f
        );
    }
}
