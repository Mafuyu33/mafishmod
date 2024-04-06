package net.mafuyu33.mafishmod.potion;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static Potion FLOWER_POTION;
    public static Potion TELEPORT_POTION;
    public static Potion SPIDER_POTION;

    public static Potion registerPotion(String name, int duration, int amplifier, StatusEffect statusEffects) {
        return Registry.register(Registries.POTION, new Identifier(TutorialMod.MOD_ID, name),
                new Potion(new StatusEffectInstance(statusEffects, duration, amplifier)));

    }

    public static void registerPotions() {
        FLOWER_POTION = registerPotion("flower_potion",3600,5,ModStatusEffects.FLOWER_EFFECT);
        TELEPORT_POTION = registerPotion("teleport_potion",100,0,ModStatusEffects.TELEPORT_EFFECT);
        SPIDER_POTION = registerPotion("spider_potion",3600,0,ModStatusEffects.SPIDER_EFFECT);
    }

    public static void registerBrewingRecipes() {

        //传送药水
        FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD,
                Ingredient.ofItems(Items.ENDER_PEARL), ModPotions.TELEPORT_POTION);
        //蜘蛛药水
        FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD,
                Ingredient.ofItems(Items.FERMENTED_SPIDER_EYE), ModPotions.SPIDER_POTION);
    }
}
