package net.mafuyu33.mafishmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.mafuyu33.mafishmod.TutorialMod;

@Config(name = TutorialMod.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public InnerLead lead = new InnerLead();
    public static class InnerLead {
        public boolean isLeadCanLinkTogether = true;
        public boolean isLeadCanLinkEveryMob = true;
        public float breakDistance = 10.0F;
    }
    public boolean isFuThrowable = true;
    public boolean isAlwaysEnchantable = true;
    public boolean isFireworkCanUseOnEntity = true;
    public boolean isFireworkCanHitOnEntity = true;
    public boolean isSpyglassCanPin = true;
    public boolean isSwimTripwire = true;
    public boolean isBowDashable = true;
    public boolean isShieldDashable = true;
    public boolean isNestedBoxInfinite = true;
//    public boolean isAlwaysEatable = true;
    @ConfigEntry.Gui.Excluded
    public boolean isBeeRideable = true;
    public boolean isGoatDashForever = false;
    public boolean isGoatDashTogether = false;
    public boolean isLlamaSpitForever = false;
//    public boolean toggleA = true;
//    public boolean toggleB = true;

//    @ConfigEntry.Gui.CollapsibleObject
//    InnerStuff stuff = new InnerStuff();
//
//    @ConfigEntry.Gui.Excluded
//    InnerStuff invisibleStuff = new InnerStuff();
//
//    static class InnerStuff {
//        int a = 0;
//        int b = 1;
//    }
}