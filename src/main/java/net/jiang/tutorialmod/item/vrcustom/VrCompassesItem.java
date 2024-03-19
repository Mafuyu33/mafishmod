package net.jiang.tutorialmod.item.vrcustom;

import net.blf02.vrapi.api.IVRAPI;
import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.particle.ParticleStorage;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VrCompassesItem extends Item{
    public VrCompassesItem(Settings settings) {
        super(settings);
    }
    private Vec3d firstPosition;
    private double red = 1.0;
    private double green = 1.0;
    private double blue = 1.0;


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(world.isClient) {
            if (VRPluginVerify.clientInVR() && VRPlugin.API.apiActive((player))) {//VR
                if (firstPosition == null) {
                    // 第一次使用直尺，记录第一个位置
                    firstPosition = getControllerPosition(player, 0);
                } else {
                    // 第二次使用直尺，记录第二个位置
                    Vec3d secondPosition = getControllerPosition(player, 0);
                    //获取颜色
                    setColor(player);
                    // 在第一次和第二次点击之间执行你想要的操作，例如生成粒子
                    generateParticlesInSphere(world, firstPosition, secondPosition,red,green,blue);

                    // 清除第一个位置
                    firstPosition = null;
                }
            }
            if(!VRPluginVerify.clientInVR()||(VRPluginVerify.clientInVR() && !VRPlugin.API.apiActive((player)))){//NOT VR
                // 获取玩家的朝向
                Vec3d lookVec = player.getRotationVector();
                double distance = 1d;
                // 将朝向向量乘以所需的距离，以确定粒子生成的位置
                double offsetX = lookVec.x * distance;
                double offsetY = lookVec.y * distance;
                double offsetZ = lookVec.z * distance;
                if (firstPosition == null) {
                    // 第一次使用直尺，记录第一个位置
                    firstPosition = new Vec3d(player.getX()+offsetX, player.getY() + offsetY + 1.625, player.getZ()+offsetZ);
                    System.out.println(firstPosition);
                } else {
                    // 第二次使用直尺，记录第二个位置
                    Vec3d secondPosition = new Vec3d(player.getX()+offsetX, player.getY() + offsetY + 1.625, player.getZ()+offsetZ);
                    System.out.println(secondPosition);
                    //获取颜色
                    setColor(player);
                    // 在第一次和第二次点击之间执行你想要的操作，例如生成粒子
                    generateParticlesInSphere(world, firstPosition, secondPosition,red,green,blue);

                    // 清除第一个位置
                    firstPosition = null;
                }
            }
        }
        return super.use(world,player,hand);
    }

    private void setColor(Entity entity) {
        if(entity.getHandItems()!=null && entity instanceof PlayerEntity) {
            Item item = ((PlayerEntity) entity).getOffHandStack().getItem();
            System.out.println(item);
            if(item==Items.RED_DYE){
                red =1.0;
                green =0.0;
                blue =0.0;
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
                green =1.0;
                blue =1.0;
            }
        }
    }

    private static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex) {
        IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
        if (vrApi != null && vrApi.apiActive(player)) {
            return vrApi.getVRPlayer(player).getController(controllerIndex).position();
        }
        return null;
    }

    private void generateParticlesBetweenTwoPositions(World world, Vec3d pos1, Vec3d pos2, double red, double green, double blue) {
        // 计算圆心到半径的距离
        double radius = pos1.distanceTo(pos2);

        // 设置粒子数量与半径相关联
        int numParticles = (int) (Math.PI * radius)*70; // 一个单位长度对应π个粒子
        System.out.println(numParticles);

        // 设置角度步长
        double angleStep = Math.PI * 2 / numParticles;

        // 从0到2PI遍历角度
        for (double angle = 0; angle < Math.PI * 2; angle += angleStep) {
            // 计算圆上点的坐标
            double posX = pos1.x + Math.cos(angle) * radius;
            double posY = pos1.y + (pos2.y - pos1.y) / 2; // 使y坐标在两点之间
            double posZ = pos1.z + Math.sin(angle) * radius;

            // 在当前位置生成粒子
            world.addParticle(ModParticles.CITRINE_PARTICLE, posX, posY, posZ, red, green, blue);
            ParticleStorage.getOrCreateForWorld().addParticle(new Vec3d(posX, posY, posZ), red, green, blue);
        }
    }

    private void generateParticlesInSphere(World world, Vec3d pos1, Vec3d pos2, double red, double green, double blue) {
        // 计算球体的半径
        double radius = pos1.distanceTo(pos2) / 2;

        // 计算球体的中心点
        Vec3d center = new Vec3d(
                (pos1.x + pos2.x) / 2,
                (pos1.y + pos2.y) / 2,
                (pos1.z + pos2.z) / 2
        );

        // 设置粒子密度，可以根据需要调整
        int density = 8; // 每个单位长度的粒子数

        // 计算立方体的边长
        double sideLength = Math.ceil(radius) * 2;

        // 计算立方体的起始位置
        double startX = center.x - sideLength / 2;
        double startY = center.y - sideLength / 2;
        double startZ = center.z - sideLength / 2;

        // 计算每个粒子的间距
        double spacing = 1.0 / density;

        // 遍历立方体中的每个位置
        for (double x = startX; x < startX + sideLength; x += spacing) {
            for (double y = startY; y < startY + sideLength; y += spacing) {
                for (double z = startZ; z < startZ + sideLength; z += spacing) {
                    // 如果当前位置到球心的距离接近于半径，则生成粒子
                    Vec3d pos = new Vec3d(x, y, z);
                    if (Math.abs(pos.distanceTo(center) - radius) < 0.5) { // 在此可以调整粒子生成的阈值
                        // 在当前位置生成粒子
                        world.addParticle(ModParticles.CITRINE_PARTICLE, x, y, z, red, green, blue);
                        ParticleStorage.getOrCreateForWorld().addParticle(pos, red, green, blue);
                    }
                }
            }
        }
    }




}
