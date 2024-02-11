package net.jiang.tutorialmod.potion;

import net.jiang.tutorialmod.TutorialMod;
import net.jiang.tutorialmod.effect.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static Potion FLOWER_POTION;

    public static Potion registerPotion(String name) {
        return Registry.register(Registries.POTION, new Identifier(TutorialMod.MOD_ID, name),
                new Potion(new StatusEffectInstance(ModStatusEffects.FLOWER_EFFECT, 3600, 5)));
    }

    public static void registerPotions() {
        FLOWER_POTION = registerPotion("flower_potion");
    }
}
