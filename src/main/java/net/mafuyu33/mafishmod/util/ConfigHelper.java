package net.mafuyu33.mafishmod.util;

import me.shedaniel.autoconfig.AutoConfig;
import net.mafuyu33.mafishmod.config.ModConfig;

public class ConfigHelper {
    public static ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public static boolean isAlwaysEnchantable() {
        return config.isAlwaysEnchantable;
    }
    public static boolean isFireworkCanUseOnEntity() {
        return config.isFireworkCanUseOnEntity;
    }
    public static boolean isFireworkCanHitOnEntity() {
        return config.isFireworkCanHitOnEntity;
    }
    public static boolean isFuThrowable() {
        return config.isFuThrowable;
    }
    public static boolean isToggleA() {
        return config.toggleA;
    }

    public static boolean isToggleB() {
        return config.toggleB;
    }

}
