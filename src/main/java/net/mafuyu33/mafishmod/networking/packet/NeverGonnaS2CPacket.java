package net.mafuyu33.mafishmod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.mafuyu33.mafishmod.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Random;

public class NeverGonnaS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
            playRandomSound(client.world,client.player);
    }
    public static void playRandomSound(World world, PlayerEntity player) {
        // 生成一个 0 到 9 之间的随机数
        int randomIndex = new Random().nextInt(10);

        // 根据随机数选择要执行的代码
        switch (randomIndex) {
            case 0:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER1, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 1:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER2, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 2:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER3, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 3:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER4, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 4:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER5, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 5:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER6, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 6:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER7, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 7:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER8, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 8:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER9, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
            case 9:
                world.playSound(player, player.getBlockPos(), ModSounds.NEVER10, SoundCategory.MASTER, 1.0f, 1.0f);
                break;
        }
    }

}
