package io.github.foundationgames.labyrinthine;

import net.fabricmc.api.ClientModInitializer;

public class LabyrinthineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Hello Client World from Labyrinthine");
    }
}
