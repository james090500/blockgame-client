package com.james090500.gui;

import com.james090500.BlockGame;
import com.james090500.gui.component.Component;
import com.james090500.gui.component.StandardButton;
import com.james090500.gui.component.TextComponent;
import com.james090500.gui.component.WorldComponent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldScreen extends Screen {

    private String selectedWorld;
    private final List<String> worlds = new ArrayList<>();

    private Component playWorld;
    private Component deleteWorld;
    private List<Component> worldComponents = new ArrayList<>();

    public WorldScreen() {
        setCloseable(false);
        setBackground(true);
        setTitle("Singleplayer");

        File path = new File("worlds");
        File[] folders = path.listFiles(File::isDirectory);
        if (folders != null) {
            for (File folder : folders) {
                worlds.add(folder.getName());
            }
        }

        init();
    }

    private void init() {
        // List the worlds
        int i = 1;
        for(String world : worlds) {
            Component worldComponent = new WorldComponent(
                    world,
                    this.width / 2 - 305,
                    75 * i,
                    610,
                    72,
                    () -> selectedWorld = world
            );
            worldComponents.add(worldComponent);
            addComponent(worldComponent);
            i++;
        }

        playWorld = StandardButton.create(
                "Play World",
                this.width / 4 - 80F,
                this.height - 110F,
                300f,
                40f,
                () -> {
                    BlockGame.getInstance().start(selectedWorld, null);
                    this.close();
                }
        );
        playWorld.setEnabled(selectedWorld != null);
        addComponent(playWorld);

        addComponent(
                StandardButton.create(
                        "New World",
                        this.width / 4 + 230,
                        this.height - 110F,
                        300f,
                        40f,
                        () -> {
                            ScreenManager.add(new NewWorldScreen());
                            this.close();
                        }
                )
        );

        deleteWorld = StandardButton.create(
                "Delete World",
                this.width / 4 - 80F,
                this.height - 60,
                300f,
                40f,
                () -> {
                    ScreenManager.add(new NewWorldScreen());
                    this.close();
                }
        );
        deleteWorld.setEnabled(false);
        addComponent(deleteWorld);

        addComponent(
                StandardButton.create(
                        "Cancel",
                        this.width / 4 + 230,
                        this.height - 60F,
                        300f,
                        40f,
                        () -> {
                            ScreenManager.add(new MainMenu());
                            this.close();
                        }
                )
        );
    }

    @Override
    public void render() {
        boolean anySelected = worldComponents.stream().anyMatch(Component::isSelected);
        playWorld.setEnabled(anySelected);
        deleteWorld.setEnabled(anySelected);

        super.render();
    }
}
