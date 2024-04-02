package net.mafuyu33.mafishmod;

import net.minecraft.server.MinecraftServer;

public class ServerManager {
    private static MinecraftServer serverInstance;

    public static void setServerInstance(MinecraftServer server) {
        serverInstance = server;
    }

    public static MinecraftServer getServerInstance() {
        return serverInstance;
    }
}

