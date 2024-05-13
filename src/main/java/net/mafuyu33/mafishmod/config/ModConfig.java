package net.mafuyu33.mafishmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.mafuyu33.mafishmod.TutorialMod;

@Config(name = TutorialMod.MOD_ID)
public class ModConfig implements ConfigData {
    public boolean isFuThrowable = true;
    public boolean isAlwaysEnchantable = true;
    public boolean isFireworkCanUseOnEntity = true;
    public boolean isFireworkCanHitOnEntity = true;
    public boolean toggleA = true;
    public boolean toggleB = true;

    @ConfigEntry.Gui.CollapsibleObject
    InnerStuff stuff = new InnerStuff();

    @ConfigEntry.Gui.Excluded
    InnerStuff invisibleStuff = new InnerStuff();

    static class InnerStuff {
        int a = 0;
        int b = 1;
    }
}