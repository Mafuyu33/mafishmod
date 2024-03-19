package net.jiang.tutorialmod.particle.custom;

import net.blf02.vrapi.api.IVRAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jiang.tutorialmod.item.vrcustom.VrRubberItem;
import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.particle.ParticleStorage;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class CitrineParticle extends SpriteBillboardParticle {
    protected CitrineParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                              SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0F;
        this.red = (float) xd;
        this.green = (float) yd;
        this.blue = (float) zd;
        this.scale *= 1F;
        this.maxAge = 999999999;
        this.setSpriteForAge(spriteSet);

        this.setColor(red, green, blue); // 设置颜色
    }

    @Override
    public float getSize(float tickDelta) {
        return 0.013F;
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
    }

    @Override
    public void tick() {
        if(world.isClient) {
            PlayerEntity player = world.getClosestPlayer(x,y,z,10,false);
            if (player!=null) {
                if (VRPluginVerify.clientInVR() && VRPlugin.API.apiActive((player))) {
                    Vec3d pos = getControllerPosition(player, 0);

                    if ((player).getActiveHand() == Hand.MAIN_HAND) {//主手用main
                        double size = 0.05;
                        Box userbox = new Box(
                                pos.x - size / 2.0, pos.y - size / 2.0, pos.z - size / 2.0, // 碰撞箱的最小顶点
                                pos.x + size / 2.0, pos.y + size / 2.0, pos.z + size / 2.0  // 碰撞箱的最大顶点
                        );
                        // 检查粒子是否在玩家周围的碰撞箱内
                        if (VrRubberItem.isErasing && isParticleInsideBox(new Vec3d(this.x,this.y,this.z),userbox)) {
                            this.age = this.maxAge - 1;
                            ParticleStorage.getOrCreateForWorld().removeParticleAtPosition(new Vec3d(this.x,this.y,this.z));
                        }

                    }
                }
//            if(!VRPluginVerify.clientInVR()||(VRPluginVerify.clientInVR() && !VRPlugin.API.apiActive((player)))){
//                System.out.println("检测中");
//                // 获取玩家的朝向
//                Vec3d lookVec = entity.getRotationVector();
//                double distance = 1d;
//                // 将朝向向量乘以所需的距离，以确定粒子生成的位置
//                double offsetX = lookVec.x * distance;
//                double offsetY = lookVec.y * distance;
//                double offsetZ = lookVec.z * distance;
//                world.addParticle(ModParticles.CITRINE_PARTICLE,
//                        entity.getX()+offsetX, entity.getY() + offsetY + 1.625, entity.getZ()+offsetZ,
//                        0, 0, 0);
//            }
            }
        }

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.velocityY -= 0.04 * (double)this.gravityStrength;
            this.move(0, 0, 0);
            if (this.ascending && this.y == this.prevPosY) {
                this.velocityX *= 1.1;
                this.velocityZ *= 1.1;
            }

            this.velocityX *= (double)this.velocityMultiplier;
            this.velocityY *= (double)this.velocityMultiplier;
            this.velocityZ *= (double)this.velocityMultiplier;
            if (this.onGround) {
                this.velocityX *= 0.699999988079071;
                this.velocityZ *= 0.699999988079071;
            }

        }
//        fadeOut();
    }

//    private void fadeOut() {
//        this.alpha = (-(1/(float)maxAge) * age + 1);
//    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CitrineParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
    private static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex) {
        IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
        if (vrApi != null && vrApi.apiActive(player)) {
            return vrApi.getVRPlayer(player).getController(controllerIndex).position();
        }
        return null;
    }
    private boolean printXYZ(){
        System.out.println(new Vec3d(x, y, z));
        return true;
    }
    private static boolean isParticleInsideBox(Vec3d position, Box collisionBox) {
        double posX = position.x;
        double posY = position.y;
        double posZ = position.z;
        double boxMinX = collisionBox.minX;
        double boxMinY = collisionBox.minY;
        double boxMinZ = collisionBox.minZ;
        double boxMaxX = collisionBox.maxX;
        double boxMaxY = collisionBox.maxY;
        double boxMaxZ = collisionBox.maxZ;

        return posX >= boxMinX && posX <= boxMaxX
                && posY >= boxMinY && posY <= boxMaxY
                && posZ >= boxMinZ && posZ <= boxMaxZ;
    }
}