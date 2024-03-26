package net.mafuyu33.mafishmod.item.vrcustom;

import net.mafuyu33.mafishmod.particle.ModParticles;
import net.mafuyu33.mafishmod.particle.ParticleStorage;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.mafuyu33.mafishmod.VRPlugin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VrRulerItem extends Item{
    public VrRulerItem(Settings settings) {
        super(settings);
    }
    private Vec3d firstPosition;
    private double red = 1.0;
    private double green = 1.0;
    private double blue = 1.0;


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(world.isClient) {
            if (VRPlugin.canRetrieveData(player)) {//VR
                if (firstPosition == null) {
                    // 第一次使用直尺，记录第一个位置
                    firstPosition = VRDataHandler.getControllerPosition(player, 0);
                } else {
                    // 第二次使用直尺，记录第二个位置
                    Vec3d secondPosition = VRDataHandler.getControllerPosition(player, 0);
                    //获取颜色
                    setPenColor(player);
                    // 在第一次和第二次点击之间执行你想要的操作，例如生成粒子
                    generateParticlesBetweenTwoPositions(world, firstPosition, secondPosition,red,green,blue);

                    // 清除第一个位置
                    firstPosition = null;
                }
            } else {//NOT VR
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
                    setPenColor(player);
                    // 在第一次和第二次点击之间执行你想要的操作，例如生成粒子
                    generateParticlesBetweenTwoPositions(world, firstPosition, secondPosition,red,green,blue);

                    // 清除第一个位置
                    firstPosition = null;
                }
            }
        }
        return super.use(world,player,hand);
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

    private void generateParticlesBetweenTwoPositions(World world, Vec3d pos1, Vec3d pos2,double red,double green,double blue) {
        // 计算两个位置之间的距离
        double distanceX = pos2.x - pos1.x;
        double distanceY = pos2.y - pos1.y;
        double distanceZ = pos2.z - pos1.z;

        // 计算在两个位置之间生成的粒子数量，这里可以根据需要调整粒子数量
        int numParticles = (int) (Math.max(Math.abs(distanceX), Math.max(Math.abs(distanceY), Math.abs(distanceZ))) * 50);
        System.out.println(numParticles);
        // 计算粒子的步长，以便在两个位置之间均匀生成粒子
        double stepX = distanceX / numParticles;
        double stepY = distanceY / numParticles;
        double stepZ = distanceZ / numParticles;

        // 从起始位置开始生成粒子
        double posX = pos1.x;
        double posY = pos1.y;
        double posZ = pos1.z;

        for (int i = 0; i < numParticles; i++) {
            // 根据步长更新粒子位置
            posX += stepX;
            posY += stepY;
            posZ += stepZ;

            // 在当前位置生成粒子
            // 这里仅作为示例，你需要根据你的粒子效果来调整生成粒子的方法
            world.addParticle(ModParticles.CITRINE_PARTICLE, posX, posY, posZ, red, green, blue);
            ParticleStorage.addParticle(new Vec3d(posX, posY, posZ), red, green, blue);
        }
    }
}
