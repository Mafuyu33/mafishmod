package net.jiang.tutorialmod.item.vrcustom;

import net.blf02.vrapi.api.IVRAPI;
import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VrPenItem extends Item {
    public VrPenItem(Settings settings) {
        super(settings);
    }

    public boolean isDrawing = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {
            isDrawing = !isDrawing;
            user.sendMessage(Text.literal((String.valueOf("切换绘画模式"))), true);
        }
        return super.use(world,user,hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(isDrawing){//画笔
            if(world.isClient) {
                if (VRPluginVerify.clientInVR() && VRPlugin.API.apiActive(((PlayerEntity) entity))) {
                    Vec3d currentPosMainController = getControllerPosition((PlayerEntity) entity, 0);
                    Vec3d currentPosOffController = getControllerPosition((PlayerEntity) entity, 1);

                    if (((PlayerEntity) entity).getActiveHand() == Hand.MAIN_HAND) {//主手用main
                        world.addParticle(ModParticles.CITRINE_PARTICLE,
                                currentPosMainController.getX(), currentPosMainController.getY(), currentPosMainController.getZ(),
                                0, 0, 0);
                    } else {//副手用off
                        world.addParticle(ModParticles.CITRINE_PARTICLE,
                                currentPosOffController.getX(), currentPosOffController.getY(), currentPosOffController.getZ(),
                                0, 0, 0);
                    }
                }
                if(!VRPluginVerify.clientInVR()||(VRPluginVerify.clientInVR() && !VRPlugin.API.apiActive(((PlayerEntity) entity)))){
                    System.out.println("生成粒子");
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
    private static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex) {
        IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
        if (vrApi != null && vrApi.apiActive(player)) {
            return vrApi.getVRPlayer(player).getController(controllerIndex).position();
        }
        return null;
    }
}
