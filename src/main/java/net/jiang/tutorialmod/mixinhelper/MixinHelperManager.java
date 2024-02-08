package net.jiang.tutorialmod.mixinhelper;

import net.minecraft.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

public class MixinHelperManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixinHelperManager.class);
    private static final Map<Entity, FireworkRocketEntityMixinHelper> helpers = new WeakHashMap<>();

    public static FireworkRocketEntityMixinHelper getHelperForEntity(Entity entity) {
        FireworkRocketEntityMixinHelper helper = helpers.computeIfAbsent(entity, e -> new FireworkRocketEntityMixinHelper());
        LOGGER.info("Getting helper for entity: {} | Helper: {}", entity, helper);
        return helper;
    }
}