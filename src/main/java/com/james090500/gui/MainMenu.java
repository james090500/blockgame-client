package com.james090500.gui;

import com.james090500.BlockGame;
import com.james090500.gui.component.Component;
import com.james090500.gui.component.StandardButton;
import com.james090500.utils.TextureManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGPaint;

import java.nio.IntBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

public class MainMenu extends Screen {
    public MainMenu() {
        setCloseable(false);
        setBackground(true);

        addComponent(
                StandardButton.create(
                    "Singleplayer",
                    this.width / 2 - 200F,
                    150f,
                    400f,
                    40f,
                    () -> {
                        ScreenManager.add(new WorldScreen());
                        this.close();
                    }
                )
        );

        Component multiplayerBtn = StandardButton.create(
                "Multiplayer",
                this.width / 2 - 200F,
                200f,
                400f,
                40f,
                this::close
        );
        multiplayerBtn.setEnabled(false);
        addComponent(multiplayerBtn);

        addComponent(
                StandardButton.create(
                    "Quit Game",
                    this.width / 2 - 150F,
                    this.height - 60F,
                    300f,
                    40f,
                    () -> BlockGame.getInstance().close()
                )
        );
    }

    @Override
    public void render() {
        addLogo();
        super.render();
    }

    private void addLogo() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        nvgImageSize(this.vg, TextureManager.logo, w, h);
        int imgWidth = w.get(0);
        int imgHeight = h.get(0);

        NVGPaint paint = NVGPaint.calloc();
        nvgImagePattern(this.vg, this.width / 2 - (float) imgWidth / 2, 0f, imgWidth, imgHeight, 0f, TextureManager.logo, 1f, paint); // alpha = 1.0

        nvgBeginPath(this.vg);
        nvgRect(this.vg, this.width / 2 - (float) imgWidth / 2, 0f, imgWidth, imgHeight); // or custom width/height
        nvgFillPaint(this.vg, paint);
        nvgFill(this.vg);

        paint.free();
    }
}
