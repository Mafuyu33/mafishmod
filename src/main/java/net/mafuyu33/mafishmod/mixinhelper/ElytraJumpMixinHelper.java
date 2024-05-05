package net.mafuyu33.mafishmod.mixinhelper;

import java.util.HashMap;
import java.util.Map;

public class ElytraJumpMixinHelper {
    private static boolean isJumpKeyPressed = false;

    // 创建一个静态Map来存储实体ID和值
    private static final Map<Integer, Integer> entityValueMap = new HashMap<>();

    // 在适当的时候将实体ID和值添加到Map中
    public static void storeEntityValue(int entityID, int value) {
        entityValueMap.put(entityID, value);
    }
    // 在需要时从Map中检索值
    public static int getEntityValue(int entityID) {
        return entityValueMap.getOrDefault(entityID, 0); // 默认值为0，如果未找到实体ID
    }
//
//    // 创建一个静态Map来存储实体ID和值
//    private static final Map<Integer, Integer> HitCoolDownMap = new HashMap<>();
//
//    // 在适当的时候将实体ID和值添加到Map中
//    public static void storeHitCoolDown(int entityID, int value) {
//        HitCoolDownMap.put(entityID, value);
//    }
//    // 在需要时从Map中检索值
//    public static int getHitCoolDown(int entityID) {
//        return HitCoolDownMap.getOrDefault(entityID, 0); // 默认值为0
//    }

    public static void setIsJumpKeyPressed(boolean isJumpKeyPressed) {
        ElytraJumpMixinHelper.isJumpKeyPressed = isJumpKeyPressed;
    }

    public static boolean isJumpKeyPressed() {
        return isJumpKeyPressed;
    }
}
