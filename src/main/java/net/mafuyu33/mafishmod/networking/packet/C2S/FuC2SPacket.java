package net.mafuyu33.mafishmod.networking.packet.C2S;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class FuC2SPacket {
    static Vec3d direction;
    static UUID uuid;
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here happens ONLY on the Server!
        // Do whatever processing you need here, then send a packet to the client to inform them of the game mode change.
        int flag = buf.readInt();
        if(flag==1) {
            direction = buf.readVec3d();
        }else if(flag==2) {
            uuid = buf.readUuid();
        }
//      itemStack.addVelocity(direction.x * horizontalSpeed, 0.3, direction.z * horizontalSpeed);
    }
    public static Vec3d getDirection(){
        return direction;
    }
    public static UUID getUuid(){
        if(uuid == null){
            return null;
        }else {
            return uuid;
        }
    }
}
