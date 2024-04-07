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

    /**
     * 判断指定的玩家是否可以获取VR相关数据，不抛出任何错误。
     */
    public static boolean canRetrieveData(PlayerEntity player){
        if (vrApi == null){
            return false;
        }
        if(player.getWorld().isClient) {
            PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            //客户端只能获取客户端玩家自身的数据
            if (clientPlayer != null) {
                return clientPlayer.equals(player) && isPlayerInVR(clientPlayer);
            }
        }

        //服务端可以获取任何玩家的数据
        return isPlayerInVR(player);
    }
}
