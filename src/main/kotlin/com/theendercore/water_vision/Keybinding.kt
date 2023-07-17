package com.theendercore.water_vision

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW


object Keybinding {
    private val configKey: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(
            "key.water_vision.config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.water_vision.generic"
        )
    )

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick
        { if (configKey.wasPressed()) config().load() })
    }
}