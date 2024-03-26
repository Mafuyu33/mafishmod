package net.mafuyu33.mafishmod.particle;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.mafuyu33.mafishmod.networking.packet.ParticleDataC2SPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.*;


public class ParticleStorage{//储存粒子和移除粒子的方法
    public static void addParticle(Vec3d position, double red, double green, double blue) {
        UUID id = UUID.randomUUID();
        sendParticleData(position,new double[]{red, green, blue},id,true);
    }

    public static void sendParticleData(Vec3d position, double[] color, UUID uuid, boolean addOrRemove) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ParticleDataC2SPacket.encodeAdd(buf, position, color, uuid, addOrRemove);
        ClientPlayNetworking.send(ModMessages.PARTICLE_DATA_ID, buf);
    }

    public static void removeParticleData(Vec3d position){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ParticleDataC2SPacket.encodeRemove(buf, position,false);
        ClientPlayNetworking.send(ModMessages.PARTICLE_DATA_ID, buf);
    }

}
