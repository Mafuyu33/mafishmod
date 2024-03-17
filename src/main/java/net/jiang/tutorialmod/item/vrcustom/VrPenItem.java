package net.jiang.tutorialmod.item.vrcustom;

import net.blf02.vrapi.api.IVRAPI;
import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.particle.custom.CitrineParticle;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.atlas.Sprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
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
    private double red = 1.0;
    private double green = 1.0;
    private double blue = 1.0;

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
                if (VRPluginVerify.clientInVR() && VRPlugin.API.apiActive(((PlayerEntity) entity))) {
                    Vec3d currentPosMainController = getControllerPosition((PlayerEntity) entity, 0);
                    Vec3d currentPosOffController = getControllerPosition((PlayerEntity) entity, 1);

                    if (((PlayerEntity) entity).getActiveHand() == Hand.MAIN_HAND) {//主手用main
                        world.addParticle(ModParticles.CITRINE_PARTICLE,
                                currentPosMainController.getX(), currentPosMainController.getY(), currentPosMainController.getZ(),
                                red, green, blue);
                    } else {//副手用off
                        world.addParticle(ModParticles.CITRINE_PARTICLE,
                                currentPosOffController.getX(), currentPosOffController.getY(), currentPosOffController.getZ(),
                                red, green, blue);
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
                            red, green, blue);
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
