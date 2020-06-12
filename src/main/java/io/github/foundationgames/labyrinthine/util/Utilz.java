package io.github.foundationgames.labyrinthine.util;

//It's Utils, but with a Z

import net.minecraft.util.Identifier;

public class Utilz {
    public static final String MOD_ID = "labyrinthine";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
