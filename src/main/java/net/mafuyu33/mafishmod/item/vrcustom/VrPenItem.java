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
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.List;

public class VrPenItem extends Item{
    public VrPenItem(Settings settings) {
        super(settings);
    }
    public boolean isDrawing = false;
    private double red = 1.0;
    private double green = 1.0;
    private double blue = 1.0;
    private int count = 0;
    private Item item;
    private Vec3d lastParticlePosition= null;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {
            isDrawing = !isDrawing;
            user.sendMessage(Text.literal((String.valueOf("切换绘画模式"))), true);

            if(isDrawing){//开始绘画，则清零上次画笔位置
                lastParticlePosition=null;
            }
        }
        return super.use(world,user,hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);


        if(isDrawing){//画笔
            if(world.isClient) {
                if(entity.getHandItems()!=null && entity instanceof PlayerEntity) {
                    item = ((PlayerEntity) entity).getOffHandStack().getItem();
                    count = ((PlayerEntity) entity).getOffHandStack().getCount();
                    setPenColor(item);
                }
//                if (Items.ENCHANTED_BOOK)
                if(count<=1) {//单画笔
                    if (entity instanceof PlayerEntity user && VRPlugin.canRetrieveData(user)) {//有MC-VR-API并且在VR中的时候
//                        float test = VRDataHandler.getControllerRoll(user, 0);
//                        System.out.println(test);
                        Vec3d currentPosMainController = VRDataHandler.getControllerPosition(user, 0);
                        Vec3d particlePosition = new Vec3d(currentPosMainController.getX(), currentPosMainController.getY(), currentPosMainController.getZ());
                        generateParticles(world, particlePosition, lastParticlePosition);
                        lastParticlePosition = particlePosition;
                    }else{
                        //获取玩家的朝向,非VR
                        Vec3d lookVec = entity.getRotationVector();
                        double distance = 1d;
                        // 将朝向向量乘以所需的距离，以确定粒子生成的位置
                        double offsetX = lookVec.x * distance;
                        double offsetY = lookVec.y * distance;
                        double offsetZ = lookVec.z * distance;
                        Vec3d particlePosition = new Vec3d(entity.getX() + offsetX, entity.getY() + offsetY + 1.625, entity.getZ() + offsetZ);
                        generateParticles(world, particlePosition, lastParticlePosition);
                        lastParticlePosition = particlePosition;
                    }
                }else {//和count相关的正方形画笔

                    if ((entity instanceof PlayerEntity user && VRPlugin.canRetrieveData(user))) {//VR

                        Vec3d currentLookAngleMainController = VRDataHandler.getControllerLookAngle(user, 0);
                        Vec3d currentPosMainController = VRDataHandler.getControllerPosition(user, 0);
                        Vec3d particlePosition = new Vec3d(currentPosMainController.getX(), currentPosMainController.getY(), currentPosMainController.getZ());
                        float controllerRoll = VRDataHandler.getControllerRoll(user, 0);
                        generateParticlesInSquareWithEdgesVR(world, particlePosition, currentLookAngleMainController,controllerRoll, count);
                    }else{//非VR
                        // 获取玩家的朝向
                        Vec3d lookVec = entity.getRotationVector();
                        double distance = 1d;
                        // 将朝向向量乘以所需的距离，以确定粒子生成的位置
                        double offsetX = lookVec.x * distance;
                        double offsetY = lookVec.y * distance;
                        double offsetZ = lookVec.z * distance;
                        Vec3d particlePosition = new Vec3d(entity.getX() + offsetX, entity.getY() + offsetY + 1.625, entity.getZ() + offsetZ);
                        generateParticlesInSquareWithEdges(world, particlePosition, lookVec, count);
                    }

                }
            }
        }
    }

    private void setPenColor(Item item) {
        if(item ==Items.WHITE_DYE){
            red =1.0;
            green =1.0;
            blue =1.0;
        }else if(item ==Items.GREEN_DYE){
            red =0.0;
            green =1.0;
            blue =0.0;
        }else if(item ==Items.BLUE_DYE){
            red =0.0;
            green =0.0;
            blue =1.0;
        }else if(item ==Items.YELLOW_DYE){
            red =1.0;
            green =1.0;
            blue =0.0;
        }else if(item ==Items.BLACK_DYE){
            red =0.0;
            green =0.0;
            blue =0.0;
        }else if(item ==Items.BROWN_DYE){
            red =0.6;
            green =0.4;
            blue =0.2;
        }else if(item ==Items.ORANGE_DYE){
            red =1.0;
            green =0.5;
            blue =0.0;
        }else if(item ==Items.MAGENTA_DYE){
            red =1.0;
            green =0.0;
            blue =1.0;
        }else if(item ==Items.LIGHT_BLUE_DYE){
            red =0.5;
            green =0.5;
            blue =1.0;
        }else if(item ==Items.LIME_DYE){
            red =0.5;
            green =1.0;
            blue =0.2;
        }else if(item ==Items.PINK_DYE){
            red =1.0;
            green =0.75;
            blue =0.8;
        }else if(item ==Items.GRAY_DYE){
            red =0.5;
            green =0.5;
            blue =0.5;
        }else if(item ==Items.LIGHT_GRAY_DYE){
            red =0.8;
            green =0.8;
            blue =0.8;
        }else if(item ==Items.CYAN_DYE){
            red =0.0;
            green =1.0;
            blue =1.0;
        }else if(item ==Items.PURPLE_DYE){
            red =0.5;
            green =0.0;
            blue =0.5;
        } else {
            red =1.0;
            green =0.0;
            blue =0.0;
        }
    }

    private void generateParticlesInSquareWithEdges(World world, Vec3d particlePosition, Vec3d currentLookAngle, int count) {
        // 计算边长
        double sideLength = count / 80.0;
        // 获取垂直于当前视线方向的向量
        Vec3d perpendicular = new Vec3d(1, 0, 0); // 默认选择 x 轴上的单位向量
        if (Math.abs(currentLookAngle.dotProduct(perpendicular)) > 0.9) {
            perpendicular = new Vec3d(0, 1, 0); // 如果当前视线方向接近于 x 轴，则选择 y 轴上的单位向量
        }
        Vec3d v1 = currentLookAngle.crossProduct(perpendicular).normalize();
        Vec3d v2 = currentLookAngle.crossProduct(v1).normalize();
        Vec3d pos1 = particlePosition.add(v1.multiply(sideLength)).add(v2.multiply(sideLength));
        Vec3d pos2 = particlePosition.add(v1.multiply(sideLength)).subtract(v2.multiply(sideLength));
        Vec3d pos3 = particlePosition.subtract(v1.multiply(sideLength)).add(v2.multiply(sideLength));
        Vec3d pos4 = particlePosition.subtract(v1.multiply(sideLength)).subtract(v2.multiply(sideLength));
        // 在正方形的四个顶点生成粒子
        generateParticles(world, pos1, pos2);
        generateParticles(world, pos2, pos4);
        generateParticles(world, pos4, pos3);
        generateParticles(world, pos3, pos1);
    }//vr
    private void generateParticlesInSquareWithEdgesVR(World world, Vec3d particlePosition, Vec3d currentLookAngle, float controllerRoll, int count) {
        double sideLength = count / 80.0;

        // 获取垂直于当前视线方向的向量
        Vec3d perpendicular = new Vec3d(1, 0, 0); // 默认选择 x 轴上的单位向量
        if (Math.abs(currentLookAngle.dotProduct(perpendicular)) > 0.9) {
            perpendicular = new Vec3d(0, 1, 0); // 如果当前视线方向接近于 x 轴，则选择 y 轴上的单位向量
        }
        Vec3d v1 = currentLookAngle.crossProduct(perpendicular).normalize();
        Vec3d v2 = currentLookAngle.crossProduct(v1).normalize();

        // 创建3x3的旋转矩阵，使用负的旋转角度
        Matrix3f rotationMatrix3x3 = new Matrix3f();
        rotationMatrix3x3.rotate(-(float) Math.toRadians(controllerRoll), new Vector3f((float) currentLookAngle.x, (float) currentLookAngle.y, (float) currentLookAngle.z));


        // 旋转向量v1和v2
        Vec3d rotatedV1 = rotateVec3d(v1, rotationMatrix3x3);
        Vec3d rotatedV2 = rotateVec3d(v2, rotationMatrix3x3);

        // 生成正方形的四个顶点，并将它们转换到手柄位置和旋转的坐标系中
        Vec3d pos1 = particlePosition.add(rotatedV1.multiply(sideLength)).add(rotatedV2.multiply(sideLength));
        Vec3d pos2 = particlePosition.add(rotatedV1.multiply(sideLength)).subtract(rotatedV2.multiply(sideLength));
        Vec3d pos3 = particlePosition.subtract(rotatedV1.multiply(sideLength)).add(rotatedV2.multiply(sideLength));
        Vec3d pos4 = particlePosition.subtract(rotatedV1.multiply(sideLength)).subtract(rotatedV2.multiply(sideLength));

        // 在转换后的正方形的四个顶点生成粒子
        generateParticles(world, pos1, pos2);
        generateParticles(world, pos2, pos4);
        generateParticles(world, pos3, pos1);
        generateParticles(world, pos4, pos3);
    }
    private void generateKnockBackParticles(World world, Vec3d particlePosition, Vec3d lastParticlePosition) {
        if (lastParticlePosition != null) {
            double distance = particlePosition.distanceTo(lastParticlePosition);
            int density = 40; // 设置粒子密度，可以根据需要调整
            int numParticles = (int) (density * distance);
            if(numParticles<=0){
                numParticles=1;
            }
            Vec3d direction = particlePosition.subtract(lastParticlePosition).normalize();
            for (int i = 1; i <= numParticles; i++) {
                double ratio = (double) i / numParticles;
                double x = lastParticlePosition.x + ratio * distance * direction.x;
                double y = lastParticlePosition.y + ratio * distance * direction.y;
                double z = lastParticlePosition.z + ratio * distance * direction.z;
                world.addParticle(ModParticles.KNOCK_BACK_PARTICLE, true, x, y, z, 1.0, 1.0, 0.0);
                ParticleStorage.addParticle(new Vec3d(x, y, z), 1.0, 1.0, 0.0);
            }
        } else {
            world.addParticle(ModParticles.KNOCK_BACK_PARTICLE, true, particlePosition.x, particlePosition.y, particlePosition.z, red, green, blue);
            ParticleStorage.addParticle(particlePosition, 1.0, 1.0, 0.0);
        }
    }

    private void generateParticles(World world, Vec3d particlePosition, Vec3d lastParticlePosition) {
        if (lastParticlePosition != null) {
            double distance = particlePosition.distanceTo(lastParticlePosition);
            int density = 40; // 设置粒子密度，可以根据需要调整
            int numParticles = (int) (density * distance);
            if(numParticles<=0){
                numParticles=1;
            }
            Vec3d direction = particlePosition.subtract(lastParticlePosition).normalize();
            for (int i = 1; i <= numParticles; i++) {
                double ratio = (double) i / numParticles;
                double x = lastParticlePosition.x + ratio * distance * direction.x;
                double y = lastParticlePosition.y + ratio * distance * direction.y;
                double z = lastParticlePosition.z + ratio * distance * direction.z;
                world.addParticle(ModParticles.CITRINE_PARTICLE, true, x, y, z, red, green, blue);
                ParticleStorage.addParticle(new Vec3d(x, y, z), red, green, blue);
            }
        } else {
            world.addParticle(ModParticles.CITRINE_PARTICLE, true, particlePosition.x, particlePosition.y, particlePosition.z, red, green, blue);
            ParticleStorage.addParticle(particlePosition, red, green, blue);
        }
    }

    private Vec3d rotateVec3d(Vec3d vec, Matrix3f rotationMatrix) {
        return new Vec3d(
                vec.x * rotationMatrix.m00 + vec.y * rotationMatrix.m01 + vec.z * rotationMatrix.m02,
                vec.x * rotationMatrix.m10 + vec.y * rotationMatrix.m11 + vec.z * rotationMatrix.m12,
                vec.x * rotationMatrix.m20 + vec.y * rotationMatrix.m21 + vec.z * rotationMatrix.m22
        );
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.mafishmod.vr_pen.tooltip"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
