package net.ace.whatmeme.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ace.WhatMeme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.ace.whatmeme.sounds.ModSounds.WHAT_SOUND_EVENT;

public class WhatMemeScreen extends Screen {
    private static final List<Identifier> IMAGES = List.of(
            Identifier.of(WhatMeme.MOD_ID, "textures/gui/what.png")
    );
    private static final int FRAME_DURATION = 260; // 每帧显示的时间（以tick为单位）
    private int frameCounter = FRAME_DURATION;
    private int currentFrame = 0;

    private PositionedSoundInstance soundInstance;
    private boolean audioPlayed = false;

    // 标志位，确保唯一实例
    private static boolean isScreenOpen = false;

    public WhatMemeScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        super.init();
        if (isScreenOpen) {
            this.close();
        } else {
            isScreenOpen = true;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);

        if (currentFrame < IMAGES.size()) {
            RenderSystem.setShaderTexture(0, IMAGES.get(currentFrame));
            RenderSystem.enableBlend(); // 启用混合模式以显示透明部分
            RenderSystem.defaultBlendFunc();
            drawContext.drawTexture(IMAGES.get(currentFrame), 0, 0, 0, 0, this.width, this.height, this.width, this.height);
            RenderSystem.disableBlend(); // 禁用混合模式

            if (!audioPlayed) {
                soundInstance = PositionedSoundInstance.master(WHAT_SOUND_EVENT, 1.0F);
                MinecraftClient.getInstance().getSoundManager().play(soundInstance);
                audioPlayed = true;
            }

            frameCounter--;
            if (frameCounter <= 0) {
                currentFrame++;
                frameCounter = FRAME_DURATION;
            }
        } else {
            // 动画结束，关闭屏幕
            this.close();
        }
    }

    @Override
    public void close() {
        // 停止音频播放
        if (soundInstance != null) {
            MinecraftClient.getInstance().getSoundManager().stop(soundInstance);
        }
        isScreenOpen = false;
        super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
