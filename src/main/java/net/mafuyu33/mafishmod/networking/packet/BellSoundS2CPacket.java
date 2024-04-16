package net.mafuyu33.mafishmod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.Sound;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class BellSoundS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here happens ONLY on the Client!
        int i=buf.readInt();
        BlockPos blockPos = buf.readBlockPos();
        System.out.println("收到！");
        System.out.println(i);
        System.out.println(blockPos);
        if(i==1 && client.world!=null){
            System.out.println("播放！");
            client.world.playSound(client.player, blockPos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS);
        }
    }
}
