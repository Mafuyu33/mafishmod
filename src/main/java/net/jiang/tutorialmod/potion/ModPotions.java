package net.jiang.tutorialmod.potion;

import net.jiang.tutorialmod.TutorialMod;
import net.jiang.tutorialmod.effect.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static Potion FLOWER_POTION;
    public static Potion TELEPORT_POTION;

    public static Potion registerPotion(String name, int duration, int amplifier, StatusEffect statusEffects) {
        return Registry.register(Registries.POTION, new Identifier(TutorialMod.MOD_ID, name),
                new Potion(new StatusEffectInstance(statusEffects, duration, amplifier)));

    }

    public static void registerPotions() {
        FLOWER_POTION = registerPotion("flower_potion",3600,5,ModStatusEffects.FLOWER_EFFECT);
        TELEPORT_POTION = registerPotion("teleport_potion",100,0,ModStatusEffects.TELEPORT_EFFECT);
    }
}
