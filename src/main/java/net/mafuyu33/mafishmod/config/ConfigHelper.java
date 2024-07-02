package net.mafuyu33.mafishmod.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.mafuyu33.mafishmod.config.ModConfig;

public class ConfigHelper {
    public static ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    public static boolean isBeeRideable() {
        return config.isBeeRideable;
    }
    public static boolean isNestedBoxInfinite() {
        return config.isNestedBoxInfinite;
    }
    public static boolean isShieldDashable() {
        return config.isShieldDashable;
    }
    public static boolean isBowDashable() {
        return config.isBowDashable;
    }
    public static boolean isSwimTripwire() {
        return config.isSwimTripwire;
    }
    public static boolean isSpyglassCanPin() {
        return config.isSpyglassCanPin;
    }

    public static boolean isLeadCanLinkEveryMob() {
        return config.lead.isLeadCanLinkEveryMob;
    }
    public static boolean isLeadCanLinkTogether() {
        return config.lead.isLeadCanLinkTogether;
    }
    public static float breakDistance() {
        return config.lead.breakDistance;
    }

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
//    public static boolean isAlwaysEatable() {
//        return config.isAlwaysEatable;
//    }
    public static boolean isGoatDashForever() {
        return config.isGoatDashForever;
    }
    public static boolean isGoatDashTogether() {
        return config.isGoatDashTogether;
    }
    public static boolean isLlamaSpitForever() {
        return config.isLlamaSpitForever;
    }
    public static boolean isQinNa() {
        return config.isQinNa;
    }



//    public static boolean isToggleA() {
//        return config.toggleA;
//    }
//
//    public static boolean isToggleB() {
//        return config.toggleB;
//    }

}
