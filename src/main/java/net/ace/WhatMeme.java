package net.ace;


import net.ace.whatmeme.Commands.WhatCommands;
import net.ace.whatmeme.Network.MyPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import static net.ace.whatmeme.sounds.ModSounds.registerSounds;

public class WhatMeme implements ModInitializer {
	public static final String MOD_ID = "what-meme";

	@Override
	public void onInitialize() {
		// 注册服务端到客户端的Payload
		PayloadTypeRegistry.playS2C().register(
				MyPayload.ID,
				MyPayload.CODEC
		);
		registerSounds();
		WhatCommands.register();
	}
}