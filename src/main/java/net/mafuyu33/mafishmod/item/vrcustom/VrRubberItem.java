package net.mafuyu33.mafishmod.item.vrcustom;

import net.mafuyu33.mafishmod.particle.ModParticles;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.mafuyu33.mafishmod.VRPlugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VrRubberItem extends Item {
    public VrRubberItem(Settings settings) {
        super(settings);
    }

    public static boolean isErasing = false;
    public static Box userbox = null;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {
            isErasing = !isErasing;
            user.sendMessage(Text.literal((String.valueOf("切换橡皮模式"))), true);
        }
        return TypedActionResult.success(user.getStackInHand(hand),world.isClient());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity instanceof PlayerEntity player) {
            if (VRPlugin.canRetrieveData(player)) {//vr
                if (VrRubberItem.isErasing) {
                    Vec3d pos = VRDataHandler.getControllerPosition(player, 0);
                    double size = (player.getOffHandStack().getCount())*0.025+0.05;
                    userbox = new Box(
                            pos.x - size / 2.0, pos.y - size / 2.0, pos.z - size / 2.0, // 碰撞箱的最小顶点
                            pos.x + size / 2.0, pos.y + size / 2.0, pos.z + size / 2.0  // 碰撞箱的最大顶点
                    );
                    collisionBoxRenderer(userbox,size);
                }
            }else{//非vr
                if (VrRubberItem.isErasing) {
                    Vec3d lookVec = player.getRotationVector();
                    double distance = 1d;
                    // 将朝向向量乘以所需的距离，以确定粒子生成的位置
                    double offsetX = lookVec.x * distance;
                    double offsetY = lookVec.y * distance;
                    double offsetZ = lookVec.z * distance;
                    Vec3d pos = new Vec3d(player.getX() + offsetX, player.getY() + offsetY + 1.625, player.getZ() + offsetZ);

                    double size = (player.getOffHandStack().getCount()) * 0.025 + 0.05;
                    userbox = new Box(
                            pos.x - size / 2.0, pos.y - size / 2.0, pos.z - size / 2.0, // 碰撞箱的最小顶点
                            pos.x + size / 2.0, pos.y + size / 2.0, pos.z + size / 2.0  // 碰撞箱的最大顶点
                    );
                    collisionBoxRenderer(userbox,size);
                }
            }
        }
    }

    private static void collisionBoxRenderer(Box userbox,double size) {
        // 获取碰撞箱的所有8个角点
        Vec3d[] corners = {
                new Vec3d(userbox.minX, userbox.minY, userbox.minZ),
                new Vec3d(userbox.minX, userbox.minY, userbox.maxZ),
                new Vec3d(userbox.minX, userbox.maxY, userbox.minZ),
                new Vec3d(userbox.minX, userbox.maxY, userbox.maxZ),
                new Vec3d(userbox.maxX, userbox.minY, userbox.minZ),
                new Vec3d(userbox.maxX, userbox.minY, userbox.maxZ),
                new Vec3d(userbox.maxX, userbox.maxY, userbox.minZ),
                new Vec3d(userbox.maxX, userbox.maxY, userbox.maxZ)
        };

        // 确定需要连接的角点索引对，来绘制立方体的12条边
        int[][] edges = {
                {0, 1}, {1, 3}, {3, 2}, {2, 0},
                {4, 5}, {5, 7}, {7, 6}, {6, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            Vec3d start = corners[edge[0]];
            Vec3d end = corners[edge[1]];
            drawParticleLine(start, end, size);
        }
    }

    private static void drawParticleLine(Vec3d start, Vec3d end, double size) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world!=null && world.isClient) {

            Vec3d direction = end.subtract(start);
            int count = (int)(size*20)+2; // 控制粒子密度
            for (int i = 0; i < count; i++) {
                double t = (double) i / (double) (count - 1);
                Vec3d point = start.add(direction.multiply(t));
                // 在计算出的点位置生成粒子
                world.addParticle(ModParticles.RUBBER_PARTICLE,true, point.x, point.y, point.z, 0, 1, 0);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.mafishmod.vr_rubber.tooltip"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
