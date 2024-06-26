package net.mafuyu33.mafishmod.networking.packet.C2S;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class ExampleC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here happens ONLY on the Server!
        // Do whatever processing you need here, then send a packet to the client to inform them of the game mode change.
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("playerGameType", GameMode.SPECTATOR.ordinal()); // 设置玩家游戏模式为观察者模式
        player.setGameMode(nbt);
    }
}
