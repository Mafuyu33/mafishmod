package net.jiang.tutorialmod;

import net.blf02.vrapi.api.data.IVRPlayer;
import net.blf02.vrapi.api.IVRAPI;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;

import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;

public class VRPlugin {
    private static IVRAPI vrApi = null;
    public static IVRAPI getVRAPI(){
        return vrApi;
    }

    public static boolean init(){
        if (vrApi != null){
            return true;
        }
        List<EntrypointContainer<IVRAPI>> entrypointList = FabricLoader.getInstance()
            .getEntrypointContainers("vrapi", IVRAPI.class);
        if (entrypointList.size() != 0)
            vrApi = entrypointList.get(0).getEntrypoint();
        return vrApi != null;
    }

    public static boolean isApiActive(PlayerEntity player){
        return player != null && vrApi != null && vrApi.apiActive(player);
    }
    public static boolean runIfApiActive(PlayerEntity player, Runnable runnable){
        if (isApiActive(player)){
            runnable.run();
            return true;
        }
        return false;
    }

    public static boolean isPlayerInVR(PlayerEntity player){
        return player != null && vrApi != null && vrApi.playerInVR(player);
    }
    public static boolean runIfPlayerInVR(PlayerEntity player, Runnable runnable){
        if (isPlayerInVR(player)){
            runnable.run();
            return true;
        }
        return false;
    }
    public static boolean runIfPlayerInVR(PlayerEntity player, Consumer<IVRPlayer> fn){
        if (isPlayerInVR(player)){
            fn.accept(vrApi.getVRPlayer(player));
            return true;
        }
        return false;
    }
    public static <R> R getIfPlayerInVR(PlayerEntity player, Function<IVRPlayer, R> fn){
        if (isPlayerInVR(player)){
            return fn.apply(vrApi.getVRPlayer(player));
        }
        return null;
    }

    public static boolean isClientInVR(){
        if (vrApi != null){
            return false;
        }
        PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer == null){
            return true; // so strange...
        }
        return isPlayerInVR(clientPlayer);
    }

    public static Vec3d getMainhandControllerPosition(PlayerEntity player){
        return getControllerPosition(player, 0);
    }
    public static Vec3d getOffhandControllerPosition(PlayerEntity player){
        return getControllerPosition(player, 1);
    }

    public static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex){
        return getIfPlayerInVR(player, vrplayer -> vrplayer.getController(controllerIndex).position());
    }
    public static Vec3d getControllerLookAngle(PlayerEntity player, int controllerIndex) {
        return getIfPlayerInVR(player, vrplayer -> vrplayer.getController(controllerIndex).getLookAngle());
    }
    public static float getControllerRoll(PlayerEntity player, int controllerIndex) {
        return getIfPlayerInVR(player, vrplayer -> vrplayer.getController(controllerIndex).getRoll());
    }
    public static Vec3d getHMDPosition(PlayerEntity player){
        return getIfPlayerInVR(player, vrplayer -> vrplayer.getHMD().position());
    }
}

/*

检测是不是在VR中
if (world.isClient && VRPluginVerify.hasAPI && VRPlugin.API.playerInVR(user)) {   //有MC-VR-API并且在VR中的时候
    user.sendMessage(Text.literal("在VR里"),false);
}else{

}
or
 if (world.isClient && VRPluginVerify.clientInVR() && VRPlugin.API.apiActive(Minecraft.getInstance().player)


两点之间绘制粒子
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
            ParticleStorage.getOrCreateForWorld().addParticle(new Vec3d(x, y, z), red, green, blue);
        }
    } else {
        world.addParticle(ModParticles.CITRINE_PARTICLE, true, particlePosition.x, particlePosition.y, particlePosition.z, red, green, blue);
        ParticleStorage.getOrCreateForWorld().addParticle(particlePosition, red, green, blue);
    }
}



获取VR玩家信息
Vec3d currentPosMainController = getControllerPosition(user,0);
Vec3d currentPosOffController = getControllerPosition(user,1);
Vec3d currentPosHMD = getHMDPosition(user);

private static Vec3d getHMDPosition(PlayerEntity player) {
    IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
    if (vrApi != null && vrApi.apiActive(player)) {
        return vrApi.getVRPlayer(player).getHMD().position();
    }
    return null;
}
private static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex) {
    IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
    if (vrApi != null && vrApi.apiActive(player)) {
        return vrApi.getVRPlayer(player).getController(controllerIndex).position();
    }
    return null;
}

*/
