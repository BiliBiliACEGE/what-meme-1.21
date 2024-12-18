package net.ace;

import net.ace.whatmeme.gui.WhatMemeScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static net.ace.whatmeme.sounds.ModSounds.registerSounds;

public class WhatMeme implements ModInitializer {
	public static final String MOD_ID = "what-meme";
	private static KeyBinding keyBinding;

	@Override
	public void onInitialize() {
		// 注册快捷键
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.what-meme.show_image_and_play_sound",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.what-meme.general"
		));

		// 注册音频事件
		registerSounds();

		// 添加按键监听
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				// 当快捷键被按下时调用显示图片和播放音频的方法
				MinecraftClient.getInstance().setScreen(new WhatMemeScreen());
			}
		});

	}
}
