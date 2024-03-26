package net.mafuyu33.mafishmod.networking.packet;

import net.mafuyu33.mafishmod.particle.StateSaverAndLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class ParticleDataC2SPacket {

    // 将粒子数据写入PacketByteBuf以供发送
    public static void encodeAdd(PacketByteBuf buf, Vec3d position, double[] color, UUID uuid, Boolean addOrRemove) {
        buf.writeBoolean(addOrRemove);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(color[0]); // R
        buf.writeDouble(color[1]); // G
        buf.writeDouble(color[2]); // B
        buf.writeUuid(uuid);
    }

    public static void encodeRemove(PacketByteBuf buf, Vec3d position, Boolean addOrRemove) {
        buf.writeBoolean(addOrRemove);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
    }
    // 在服务器端处理接收到的数据包
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        boolean addOrRemove = buf.readBoolean();
        Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);
        if(addOrRemove) {
            double[] color = new double[]{buf.readDouble(), buf.readDouble(), buf.readDouble()};
            UUID uuid = buf.readUuid(); // 读取UUID
            state.particles.add(new StateSaverAndLoader.ParticleInfo(position, color, uuid));
        }else {
            StateSaverAndLoader.getServerState(server).removeParticleByPosition(position);
        }
    }

}

