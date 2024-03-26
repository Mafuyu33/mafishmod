//package net.mafuyu33.mafishmod.networking.packet;
//
//import net.fabricmc.fabric.api.networking.v1.PacketSender;
//import net.mafuyu33.mafishmod.mixinhelper.BowDashMixinHelper;
//import net.mafuyu33.mafishmod.particle.ParticleStorage;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayNetworkHandler;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.util.math.Box;
//import net.minecraft.util.math.Vec3d;
//
//import java.util.List;
//
//public class ParticleColorC2SPacket {
//
//    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
//                               PacketByteBuf buf, PacketSender responseSender) {
//            int id = buf.readInt();
////            Vec3d pos = ParticleStorage.getOrCreateForWorld().getUUIDByPosition();
//        if(pos!=null) {
//            //想办法让碰到这个粒子的生物（除了玩家被击退+受伤）
//            // 获取范围内的所有生物
//            Box box1 = new Box(pos.x - 1.0, pos.y - 1.0, pos.z - 1.0, pos.x + 1.0, pos.y + 1.0, pos.z + 1.0);
//            Box box2 = new Box(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 0.1, pos.y + 0.1, pos.z + 0.1);
//            List<Entity> entities = player.getWorld().getOtherEntities(null, box1, entity -> entity instanceof LivingEntity && !(entity instanceof PlayerEntity));
//            // 遍历每个生物并检查碰撞
//            for (Entity entity : entities) {
//                if (entity.getBoundingBox().intersects(box2)) {
//                    System.out.println("碰撞!");
//                    // 碰到生物时，除了玩家之外，对其进行击退和受伤
//                    if (entity instanceof LivingEntity livingEntity) {
//                        System.out.println("造成伤害！");
//                        Vec3d knockBackVec = new Vec3d(entity.getX() - pos.x, entity.getY() - pos.y, entity.getZ() - pos.z).normalize().multiply(-1.0); // 计算击退向量
//                        livingEntity.takeKnockback(1.0f, knockBackVec.x, knockBackVec.z); // 进行击退
//                        livingEntity.damage(player.getWorld().getDamageSources().magic(), 1.0f); // 造成伤害
//                    }
//                }
//            }
//        }
//    }
//}
