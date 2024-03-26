package net.mafuyu33.mafishmod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.mafuyu33.mafishmod.mixinhelper.ShieldDashMixinHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ShieldDashC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        ShieldDashMixinHelper.storeHitCoolDown(player.getId(),buf.getInt(0));

    }
}
