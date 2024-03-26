package net.mafuyu33.mafishmod.mixinhelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FearMixinHelper {
    // 创建一个静态Map来存储实体ID和值
    private static final Map<UUID, Integer> entityValueMap = new HashMap<>();

    // 在适当的时候将实体ID和值添加到Map中
    public static void storeEntityValue(UUID entityID, int value) {
        entityValueMap.put(entityID, value);
    }
    // 在需要时从Map中检索值
    public static int getEntityValue(UUID entityID) {
        return entityValueMap.getOrDefault(entityID, 0); // 默认值为0，如果未找到实体ID
    }
    // 创建一个静态Map来存储实体ID和值
    private static final Map<UUID, Boolean> isAttackedMap = new HashMap<>();

    // 在适当的时候将实体ID和值添加到Map中
    public static void storeIsAttacked(UUID entityID, Boolean value) {
        isAttackedMap.put(entityID, value);
    }
    // 在需要时从Map中检索值
    public static Boolean getIsAttacked(UUID entityID) {
        return isAttackedMap.getOrDefault(entityID, false); // 默认值为0，如果未找到实体ID
    }

    // 创建一个静态Map来存储实体ID和值
    private static final Map<UUID, Boolean> isFirstTimeMap = new HashMap<>();

    // 在适当的时候将实体ID和值添加到Map中
    public static void setIsFirstTime(UUID entityID, Boolean value) {
        isFirstTimeMap.put(entityID, value);
    }
    // 在需要时从Map中检索值
    public static Boolean getIsFirstTime(UUID entityID) {
        return isFirstTimeMap.getOrDefault(entityID, true); // 默认值为0，如果未找到实体ID
    }
}
