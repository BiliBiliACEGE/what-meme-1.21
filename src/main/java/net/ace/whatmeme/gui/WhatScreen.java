package net.ace.whatmeme.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ace.WhatMeme;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.ace.whatmeme.sounds.ModSounds.WHAT_SOUND_EVENT;
@Environment(EnvType.CLIENT)
public class WhatScreen extends Screen {
    private static final List<Identifier> IMAGES = List.of(
            Identifier.of(WhatMeme.MOD_ID, "textures/gui/what.png")
    );
    private static final int DEFAULT_TICKS = 260; // 默认持续时间

    // 实际使用的持续时间（默认值或指令传入值）
    private final int totalDurationTicks;
    private int remainingTicks;
    private int currentFrame = 0;

    private PositionedSoundInstance soundInstance;
    private boolean audioPlayed = false;
    private static boolean isScreenOpen = false;

    // 默认构造函数（用于非指令情况）
    public WhatScreen() {
        this(DEFAULT_TICKS); // 调用主构造函数并传入默认值
    }

    // 主构造函数（用于指令传入参数）
    public WhatScreen(int durationTicks) {
        super(Text.of(""));
        // 使用三元运算符确保最小持续时间
        this.totalDurationTicks = Math.max(durationTicks, 10); // 至少10ticks
        this.remainingTicks = this.totalDurationTicks;
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

            remainingTicks--;
            if (remainingTicks <= 0) {
                currentFrame++;
                remainingTicks = totalDurationTicks;
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