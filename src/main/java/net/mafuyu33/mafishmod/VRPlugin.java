package net.mafuyu33.mafishmod;

import net.blf02.vrapi.api.data.IVRPlayer;
import net.blf02.vrapi.api.IVRAPI;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;

import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;

public class VRPlugin {
    private static final int[] COMPATIBLE_VRAPI_VERSION = {3, 0, 3};
    private static final String COMPATIBLE_VRAPI_VERSION_STRING = "3.0.3";

    private static Object vrApi = null;

    /**
     * 获取VRAPI实例。
     *
     * 注意：在这之前调用 hasVRAPI() 进行检查，否则可能会出现无法预料的错误。
     */
    public static IVRAPI getVRAPI(){
        return (IVRAPI)vrApi;
    }
    public static boolean hasVRAPI(){
        return vrApi != null;
    }

    private static boolean checkVRAPIVersion(IVRAPI api) {
        int[] versionArray;
        try {
            versionArray = api.getVersionArray();
            if (versionArray == null || versionArray.length != 3) {
                TutorialMod.LOGGER.warn("invalid version array return from vrapi!");
                return false;
            }
        } catch (Throwable ex) {
            TutorialMod.LOGGER.error("unknown error while checking version for vrapi", ex);
            return false;
        }

        int majorVersion = versionArray[0];
        int minorVersion = versionArray[1];
        int patchVersion = versionArray[2];

        //主版本号不匹配则不兼容
        if (majorVersion != COMPATIBLE_VRAPI_VERSION[0]) {
            TutorialMod.LOGGER.warn("found vrapi, but its major version is {}, which is not be compatible with {} ({})", majorVersion, COMPATIBLE_VRAPI_VERSION[0], COMPATIBLE_VRAPI_VERSION_STRING);
            return false;
        }

        if (minorVersion > COMPATIBLE_VRAPI_VERSION[1]) {
            //副版本号更大只警告
            //这也许会有一点问题，但是我觉得可以接受
            TutorialMod.LOGGER.warn("found vrapi, but its minor version is {}, which may not be compatible with {} ({}). Use it at your onw risk!", minorVersion, COMPATIBLE_VRAPI_VERSION[1], COMPATIBLE_VRAPI_VERSION_STRING);
        } else if (minorVersion < COMPATIBLE_VRAPI_VERSION[1]) {
            //副版本号更小，不兼容
            TutorialMod.LOGGER.warn("found vrapi, but its minor version is {}, which is not be compatible with {} ({}).", minorVersion, COMPATIBLE_VRAPI_VERSION[1], COMPATIBLE_VRAPI_VERSION_STRING);
            return false;
        } else {
            // 副版本号一致，检查补充版本号
            if (patchVersion < COMPATIBLE_VRAPI_VERSION[2]){
                //补充版本号更小，不兼容
                TutorialMod.LOGGER.warn("found vrapi, but its patch version is {}, which is not be compatible with {} ({}).", patchVersion, COMPATIBLE_VRAPI_VERSION[2], COMPATIBLE_VRAPI_VERSION_STRING);
                return false;
            }
        }

        // 兼容的版本
        return true;
    }

    public static boolean init(){
        if (hasVRAPI()){
            return true;
        }
        try {
            Class.forName("net.blf02.vrapi.api.IVRAPI");
        } catch (ClassNotFoundException ex){
            TutorialMod.LOGGER.info("vrapi not loaded, ignoring!");
            return false;
        }

        IVRAPI providedVrApi = null;

        List<EntrypointContainer<IVRAPI>> entrypointList = FabricLoader.getInstance()
            .getEntrypointContainers("vrapi", IVRAPI.class);
        if (entrypointList.size() != 0)
            providedVrApi = entrypointList.get(0).getEntrypoint();

        if (providedVrApi != null && checkVRAPIVersion(providedVrApi)){
            TutorialMod.LOGGER.info("compatible version of vrapi found, VR features will be enabled if player has VR");
            vrApi = providedVrApi;
        } else {
            TutorialMod.LOGGER.info("no compatible version of vrapi found, VR features will not work");
        }

        return vrApi != null;
    }

    public static boolean isApiActive(PlayerEntity player){
        return player != null && hasVRAPI() && getVRAPI().apiActive(player);
    }
    public static boolean runIfApiActive(PlayerEntity player, Runnable runnable){
        if (isApiActive(player)){
            runnable.run();
            return true;
        }
        return false;
    }

    public static boolean isPlayerInVR(PlayerEntity player){
        return player != null && hasVRAPI() && getVRAPI().playerInVR(player);
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
            fn.accept(getVRAPI().getVRPlayer(player));
            return true;
        }
        return false;
    }
    public static <R> R getIfPlayerInVR(PlayerEntity player, Function<IVRPlayer, R> fn){
        if (isPlayerInVR(player)){
            return fn.apply(getVRAPI().getVRPlayer(player));
        }
        return null;
    }

    /**
     * 判断指定的玩家是否可以获取VR相关数据，不抛出任何错误。
     */
    public static boolean canRetrieveData(PlayerEntity player){
        if (!hasVRAPI()){
            return false;
        }

        if(player.getWorld().isClient) {
                return player == MinecraftClient.getInstance().player && isPlayerInVR(player);
        }

        //服务端可以获取任何玩家的数据
        return isPlayerInVR(player);
    }
}
