package net.mafuyu33.mafishmod.item.vrcustom;

import net.mafuyu33.mafishmod.particle.ModParticles;
import net.mafuyu33.mafishmod.sound.ModSounds;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.mafuyu33.mafishmod.VRPlugin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class VrMagicItem extends Item {
    public VrMagicItem(Settings settings) {
        super(settings);
    }

    public static boolean isUsingMagic = false;
    private final float red = 0.8f;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            isUsingMagic = !isUsingMagic;
        }
        if(world.isClient){
            if (!isUsingMagic) {
                world.playSound(user, user.getBlockPos(), ModSounds.METAL_DETECTOR_FOUND_ORE, SoundCategory.PLAYERS);
            }
        }
        return super.use(world,user,hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            if (VRPlugin.canRetrieveData(player)) {
                if (isUsingMagic) {
                    Vec3d currentLookAngleMainController = VRDataHandler.getControllerLookAngle((PlayerEntity) entity, 0);
                    Vec3d currentLookAngleOffController = VRDataHandler.getControllerLookAngle((PlayerEntity) entity, 1);
                    Vec3d currentPosMainController = VRDataHandler.getControllerPosition((PlayerEntity) entity, 0);
                    Vec3d currentPosOffController = VRDataHandler.getControllerPosition((PlayerEntity) entity, 1);
                    float mainControllerRoll = VRDataHandler.getControllerRoll(((PlayerEntity) entity),0);
                    float offControllerRoll = VRDataHandler.getControllerRoll(((PlayerEntity) entity),1);
                    if(currentLookAngleMainController!=null) {
                        generateParticlesInMagicWithEdgesVR(world, currentPosMainController, currentLookAngleMainController, mainControllerRoll,red,0,0);
                    }
                    if(currentLookAngleOffController!=null) {
                        generateParticlesInMagicWithEdgesVR(world, currentPosOffController, currentLookAngleOffController, offControllerRoll,red,0,0);
                    }
                }
            } else {
                player.sendMessage(Text.literal("sorry, this item currently only working with VR Mode :("), false);
            }
        }
    }

    private void generateParticlesInMagicWithEdgesVR(World world, Vec3d particlePosition, Vec3d currentLookAngle, float controllerRoll,float red,float green,float blue) {
        int numVertices = 350; // 圆上的顶点数
        double sideLength = 0.25;
        double radius = sideLength * Math.sqrt(3)/1.5; // 外圆半径为正三角形边长的sqrt(3)/1.5
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
        Vec3d rotatedV1 = VRDataHandler.rotateVec3d(v1, rotationMatrix3x3);
        Vec3d rotatedV2 = VRDataHandler.rotateVec3d(v2, rotationMatrix3x3);



        // 生成正三角形的三个顶点
        Vec3d triangleVertex1 = particlePosition.add(rotatedV1.multiply(sideLength));
        Vec3d triangleVertex2 = particlePosition.add(rotatedV1.multiply(-sideLength / 2)).subtract(rotatedV2.multiply(Math.sqrt(3) * sideLength / 2));
        Vec3d triangleVertex3 = particlePosition.add(rotatedV1.multiply(-sideLength / 2)).add(rotatedV2.multiply(Math.sqrt(3) * sideLength / 2));

        // 生成倒三角形的三个顶点
        Vec3d invertedTriangleVertex1 = particlePosition.subtract(rotatedV1.multiply(sideLength));
        Vec3d invertedTriangleVertex2 = particlePosition.subtract(rotatedV1.multiply(-sideLength / 2)).add(rotatedV2.multiply(Math.sqrt(3) * sideLength / 2));
        Vec3d invertedTriangleVertex3 = particlePosition.subtract(rotatedV1.multiply(-sideLength / 2)).subtract(rotatedV2.multiply(Math.sqrt(3) * sideLength / 2));

        // 生成圆形的顶点，并将它们转换到手柄位置和旋转的坐标系中
        for (int i = 0; i < numVertices; i++) {
            double angle = 2 * Math.PI * i / numVertices;
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;

            // 计算顶点在旋转后的坐标系中的位置
            Vec3d rotatedVertex = particlePosition.add(rotatedV1.multiply(x)).add(rotatedV2.multiply(y));
            // 在转换后的圆形的顶点生成粒子
            world.addParticle(ModParticles.RUBBER_PARTICLE, true, rotatedVertex.x, rotatedVertex.y, rotatedVertex.z, red, green, blue);
        }

        // 生成三角形和倒三角形的边
        generateParticles(world, triangleVertex1, triangleVertex2);
        generateParticles(world, triangleVertex2, triangleVertex3);
        generateParticles(world, triangleVertex3, triangleVertex1);
        generateParticles(world, invertedTriangleVertex1, invertedTriangleVertex2);
        generateParticles(world, invertedTriangleVertex2, invertedTriangleVertex3);
        generateParticles(world, invertedTriangleVertex3, invertedTriangleVertex1);
    }


    private void generateParticles(World world, Vec3d particlePosition, Vec3d lastParticlePosition) {
        if (lastParticlePosition != null) {
            double distance = particlePosition.distanceTo(lastParticlePosition);
            int density = 200; // 设置粒子密度，可以根据需要调整
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
                world.addParticle(ModParticles.RUBBER_PARTICLE, true, x, y, z,  red, 0, 0);
            }
        } else {
            world.addParticle(ModParticles.RUBBER_PARTICLE, true, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
        }
    }

}
