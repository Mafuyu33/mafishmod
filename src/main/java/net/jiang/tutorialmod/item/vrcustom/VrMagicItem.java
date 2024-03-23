package net.jiang.tutorialmod.item.vrcustom;

import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.VRPlugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VrMagicItem extends Item {
    public VrMagicItem(Settings settings) {
        super(settings);
    }

    public static boolean isUsingMagic = false;
    public static Box userBox = null;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            isUsingMagic = !isUsingMagic;
            if (isUsingMagic) {
                world.playSound(user, user.getBlockPos(), SoundEvents.MUSIC_DISC_FAR, SoundCategory.VOICE);
            }
        }
        return super.use(world,user,hand);
    }

//    @Override
//    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
//        super.inventoryTick(stack, world, entity, slot, selected);
//        if (entity instanceof PlayerEntity player) {
//            if (VRPlugin.isClientInVR() && VRPlugin.isPlayerInVR(player)) {
//                if (VrMagicItem.isUsingMagic) {
//                    Vec3d pos = VRPlugin.getControllerPosition(player, 0);
//                    double size = (player.getOffHandStack().getCount()) * 0.025 + 0.05;
//                    userBox = new Box(
//                            pos.x - size / 2.0, pos.y - size / 2.0, pos.z - size / 2.0,
//                            pos.x + size / 2.0, pos.y + size / 2.0, pos.z + size / 2.0
//                    );
//                    renderMagicCircle(userBox, size);
//                }
//            }
//        }
//    }

    private static void renderMagicCircle(Box userBox, double size) {
        // 获取碰撞箱的中心点
        Vec3d center = new Vec3d(
                (userBox.minX + userBox.maxX) / 2.0,
                (userBox.minY + userBox.maxY) / 2.0,
                (userBox.minZ + userBox.maxZ) / 2.0
        );

        // 获取碰撞箱的尺寸
        double width = userBox.getLengthX();
        double height = userBox.getLengthY();
        double depth = userBox.getLengthZ();

        // 定义魔法阵的尺寸
        double circleRadius = Math.min(Math.min(width, height), depth) / 2.0;
        double lineThickness = size / 10.0; // 魔法阵线条的厚度

        // 计算魔法阵的顶点
        Vec3d[] circlePoints = new Vec3d[360];
        for (int i = 0; i < 360; i++) {
            double angle = Math.toRadians(i);
            double x = center.x + circleRadius * Math.cos(angle);
            double y = center.y;
            double z = center.z + circleRadius * Math.sin(angle);
            circlePoints[i] = new Vec3d(x, y, z);
        }

        // 渲染魔法阵的圆形部分
        for (int i = 0; i < circlePoints.length - 1; i++) {
            Vec3d start = circlePoints[i];
            Vec3d end = circlePoints[i + 1];
            renderMagicLine(start, end, lineThickness);
        }
        renderMagicLine(circlePoints[circlePoints.length - 1], circlePoints[0], lineThickness); // 连接最后一个点和第一个点，形成闭合圆形
    }

    private static void renderMagicLine(Vec3d start, Vec3d end, double thickness) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null && world.isClient) {
            Vec3d direction = end.subtract(start).normalize();
            Vec3d perpendicular = new Vec3d(-direction.z, 0, direction.x).normalize().multiply(thickness / 2.0); // 计算与线段方向垂直的向量并乘以厚度的一半

            // 计算线段的四个顶点
            Vec3d vertex1 = start.add(perpendicular);
            Vec3d vertex2 = start.subtract(perpendicular);
            Vec3d vertex3 = end.add(perpendicular);
            Vec3d vertex4 = end.subtract(perpendicular);

            // 渲染线段的两个三角形
            world.addParticle(ModParticles.RUBBER_PARTICLE, true, vertex1.x, vertex1.y, vertex1.z, 0, 1, 0);
            world.addParticle(ModParticles.RUBBER_PARTICLE, true, vertex2.x, vertex2.y, vertex2.z, 0, 1, 0);
            world.addParticle(ModParticles.RUBBER_PARTICLE, true, vertex3.x, vertex3.y, vertex3.z, 0, 1, 0);
            world.addParticle(ModParticles.RUBBER_PARTICLE, true, vertex4.x, vertex4.y, vertex4.z, 0, 1, 0);
        }
    }
}
