package com.james090500.client;

import com.james090500.BlockGame;
import com.james090500.gui.Screen;
import com.james090500.gui.ScreenManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ClientInput {

    private boolean firstMouse = true;
    private double lastMouseX;
    private double lastMouseY;

    @Getter
    HashMap<Integer, Boolean> mouse = new HashMap<>();
    @Getter
    HashMap<Integer, Boolean> keys = new HashMap<>();

    public void mouseMovement(long w, double xpos, double ypos) {
        //Skip if paused
        if(BlockGame.getInstance().getConfig().isPaused())
            return;

        if (firstMouse) {
            lastMouseX = xpos;
            lastMouseY = ypos;
            firstMouse = false;
        }
        float sensitivity = 0.1f;
        double dx = xpos - lastMouseX;
        double dy = lastMouseY - ypos;

        lastMouseX = xpos;
        lastMouseY = ypos;

        Camera camera = BlockGame.getInstance().getCamera();
        camera.yaw += (float) (dx * sensitivity);
        camera.pitch += (float) (dy * sensitivity);
        camera.pitch = Math.max(-89f, Math.min(89f, camera.pitch));

        camera.updateFrustum();
    }

    public void mouseClicked(long win, int button, int action, int mods) {
        mouse.put(button, action > GLFW_RELEASE);

        if (mouse.getOrDefault(GLFW_MOUSE_BUTTON_LEFT, false)) {
            this.leftClick();
        }
    }

    public void mouseScroll(long window, double xOffset, double yOffset) {
        if (xOffset > 0) {
           BlockGame.getInstance().getLocalPlayer().changeHand(1);
        } else if (xOffset < 0) {
            BlockGame.getInstance().getLocalPlayer().changeHand(-1);
        }
    }

    public void leftClick() {
        List<Screen> screens = new ArrayList<>(ScreenManager.active());
        for (Screen screen : screens) {
            screen.click();
        }
    }

    public void keyPressed(long window, int key, int scancode, int action, int mods) {
        keys.put(key, action > GLFW_RELEASE);

        if (keys.getOrDefault(GLFW_KEY_ESCAPE, false)) {
            if (ScreenManager.active().stream().anyMatch(Screen::isCloseable)) {
                ScreenManager.clear();
            } else if(!BlockGame.getInstance().getConfig().isPaused()) {
                BlockGame.getInstance().pause();
            }
        }

        if(keys.getOrDefault(GLFW_KEY_BACKSPACE, false)) {
            List<Screen> screens = new ArrayList<>(ScreenManager.active());
            for (Screen screen : screens) {
                screen.type(-1);
            }
        }
    }

    public void keyTyped(long window, int codepoint) {
        List<Screen> screens = new ArrayList<>(ScreenManager.active());
        for (Screen screen : screens) {
            screen.type(codepoint);
        }
    }
}
