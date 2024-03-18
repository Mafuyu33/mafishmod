package net.jiang.tutorialmod.item.vrcustom;

import net.blf02.vrapi.api.IVRAPI;
import net.jiang.tutorialmod.mixinhelper.VrRubberItemHelper;
import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class VrRubberItem extends Item {
    public VrRubberItem(Settings settings) {
        super(settings);
    }

    public boolean isErasing = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {
            isErasing = !isErasing;
//            VrRubberItemHelper.
//            user.sendMessage(Text.literal((String.valueOf("切换橡皮模式"))), true);
        }
        return super.use(world,user,hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(isErasing){//橡皮
            if(world.isClient) {
                if (VRPluginVerify.clientInVR() && VRPlugin.API.apiActive(((PlayerEntity) entity))) {
                    Vec3d currentPosMainController = getControllerPosition((PlayerEntity) entity, 0);

                    if (((PlayerEntity) entity).getActiveHand() == Hand.MAIN_HAND) {//主手用main
                        // 遍历当前世界中的所有粒子
                        // 检查粒子是否在玩家周围的碰撞箱内
//                        if (isParticleInsideBox(particle,currentPosMainController)) {
//                            particle
//                        }
                    }
                }
                if(!VRPluginVerify.clientInVR()||(VRPluginVerify.clientInVR() && !VRPlugin.API.apiActive(((PlayerEntity) entity)))){
                    System.out.println("检测中");
                    // 获取玩家的朝向
                    Vec3d lookVec = entity.getRotationVector();
                    double distance = 1d;
                    // 将朝向向量乘以所需的距离，以确定粒子生成的位置
                    double offsetX = lookVec.x * distance;
                    double offsetY = lookVec.y * distance;
                    double offsetZ = lookVec.z * distance;
                    world.addParticle(ModParticles.CITRINE_PARTICLE,
                            entity.getX()+offsetX, entity.getY() + offsetY + 1.625, entity.getZ()+offsetZ,
                            0, 0, 0);
                }
            }
        }
    }

    private boolean isParticleInsideBox(Particle particle,Vec3d pos) {
        double size = 2.0;
        Box userbox = new Box(
                pos.x - size / 2.0, pos.y - size / 2.0, pos.z - size / 2.0, // 碰撞箱的最小顶点
                pos.x + size / 2.0, pos.y + size / 2.0, pos.z + size / 2.0  // 碰撞箱的最大顶点
        );
        Box particleBox = particle.getBoundingBox();
        return userbox.intersects(particleBox);
    }
    private static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex) {
        IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
        if (vrApi != null && vrApi.apiActive(player)) {
            return vrApi.getVRPlayer(player).getController(controllerIndex).position();
        }
        return null;
    }
}
