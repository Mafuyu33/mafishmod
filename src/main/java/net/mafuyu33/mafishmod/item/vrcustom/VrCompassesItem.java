package net.mafuyu33.mafishmod.item.vrcustom;

import net.mafuyu33.mafishmod.particle.ModParticles;
import net.mafuyu33.mafishmod.particle.ParticleStorage;
import net.mafuyu33.mafishmod.VRPlugin;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VrCompassesItem extends Item{
    public VrCompassesItem(Settings settings) {
        super(settings);
    }
    private Vec3d firstPosition;
    private Vec3d secondPosition;
    private double red = 1.0;
    private double green = 1.0;
    private double blue = 1.0;


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(world.isClient) {
            if (VRPlugin.canRetrieveData(player)) {//VR
                if (firstPosition == null && secondPosition ==null) {
                    // 第一次使用直尺，记录第一个位置
                    firstPosition = VRDataHandler.getControllerPosition(player, 0);
                } else if(secondPosition==null) {
                    // 第二次使用直尺，记录第二个位置
                    secondPosition = VRDataHandler.getControllerPosition(player, 0);
                }else {
                    Vec3d thirdPosition = VRDataHandler.getControllerPosition(player, 0);
                    //获取颜色
                    setPenColor(player);
                    //生成圆
                    generateParticlesOnCircle(world, firstPosition, secondPosition, thirdPosition,red,green,blue);
                    // 清除位置
                    firstPosition = null;
                    secondPosition= null;
                }
            } else{//NOT VR
                // 获取玩家的朝向
                Vec3d lookVec = player.getRotationVector();
                double distance = 1d;
                // 将朝向向量乘以所需的距离，以确定粒子生成的位置
                double offsetX = lookVec.x * distance;
                double offsetY = lookVec.y * distance;
                double offsetZ = lookVec.z * distance;
                Vec3d pos = new Vec3d(player.getX()+offsetX, player.getY() + offsetY + 1.625, player.getZ()+offsetZ);

                if (firstPosition == null && secondPosition ==null) {
                    // 第一次使用直尺，记录第一个位置
                    firstPosition = pos;
                } else if(secondPosition==null) {
                    // 第二次使用直尺，记录第二个位置
                    secondPosition = pos;
                }else {
                    Vec3d thirdPosition = pos;
                    //获取颜色
                    setPenColor(player);
                    //生成圆
                    generateParticlesOnCircle(world, firstPosition, secondPosition, thirdPosition,red,green,blue);
                    // 清除位置
                    firstPosition = null;
                    secondPosition= null;
                }
            }
        }
        return super.use(world,player,hand);
    }

    private void generateParticlesOnCircle(World world, Vec3d center, Vec3d radiusPoint, Vec3d thirdPoint, double red, double green, double blue) {
        // 计算半径
        double radius = center.distanceTo(radiusPoint);
        // 计算圆周上的点数量
        int density = 40;
        int numParticles = (int) (2 * Math.PI * radius * density);
        System.out.println(numParticles);

        // 计算法向量
        Vec3d normal = radiusPoint.subtract(center).crossProduct(thirdPoint.subtract(center)).normalize();

        // 找到两个与圆心向量垂直的向量
        Vec3d v1 = radiusPoint.subtract(center).normalize(); // 从圆心指向第一个点，得到一个向量
        Vec3d v2 = normal.crossProduct(v1).normalize(); // 使用法向量和v1的叉积得到一个与法向量和v1都垂直的向量


        // 计算圆周上的点
        for (int i = 0; i < numParticles; i++) {
            double angle = 2 * Math.PI * i / numParticles;
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
            // 使用两个向量和半径来计算圆周上的点
            double x = center.x + radius * (cosAngle * v1.getX() + sinAngle * v2.getX());
            double y = center.y + radius * (cosAngle * v1.getY() + sinAngle * v2.getY());
            double z = center.z + radius * (cosAngle * v1.getZ() + sinAngle * v2.getZ());

            // 在当前位置生成粒子
            System.out.println(new Vec3d(x,y,z));
            world.addParticle(ModParticles.CITRINE_PARTICLE, x, y, z, red, green, blue);
            ParticleStorage.addParticle(new Vec3d(x, y, z), red, green, blue);
        }
    }





    private void setPenColor(Entity entity) {
        if(entity.getHandItems()!=null && entity instanceof PlayerEntity) {
            Item item = ((PlayerEntity) entity).getOffHandStack().getItem();
            System.out.println(item);
            if(item==Items.WHITE_DYE){
                red =1.0;
                green =1.0;
                blue =1.0;
            }else if(item==Items.GREEN_DYE){
                red =0.0;
                green =1.0;
                blue =0.0;
            }else if(item==Items.BLUE_DYE){
                red =0.0;
                green =0.0;
                blue =1.0;
            }else if(item==Items.YELLOW_DYE){
                red =1.0;
                green =1.0;
                blue =0.0;
            }else if(item==Items.BLACK_DYE){
                red =0.0;
                green =0.0;
                blue =0.0;
            }else if(item==Items.BROWN_DYE){
                red =0.6;
                green =0.4;
                blue =0.2;
            }else if(item==Items.ORANGE_DYE){
                red =1.0;
                green =0.5;
                blue =0.0;
            }else if(item==Items.MAGENTA_DYE){
                red =1.0;
                green =0.0;
                blue =1.0;
            }else if(item==Items.LIGHT_BLUE_DYE){
                red =0.5;
                green =0.5;
                blue =1.0;
            }else if(item==Items.LIME_DYE){
                red =0.5;
                green =1.0;
                blue =0.2;
            }else if(item==Items.PINK_DYE){
                red =1.0;
                green =0.75;
                blue =0.8;
            }else if(item==Items.GRAY_DYE){
                red =0.5;
                green =0.5;
                blue =0.5;
            }else if(item==Items.LIGHT_GRAY_DYE){
                red =0.8;
                green =0.8;
                blue =0.8;
            }else if(item==Items.CYAN_DYE){
                red =0.0;
                green =1.0;
                blue =1.0;
            }else if(item==Items.PURPLE_DYE){
                red =0.5;
                green =0.0;
                blue =0.5;
            } else {
                red =1.0;
                green =0.0;
                blue =0.0;
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.mafishmod.vr_compasses.tooltip"));
        super.appendTooltip(stack, world, tooltip, context);
    }

}







