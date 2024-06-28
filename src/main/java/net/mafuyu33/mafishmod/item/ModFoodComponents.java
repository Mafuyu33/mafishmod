package net.mafuyu33.mafishmod.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent TOMATO=new FoodComponent.Builder().hunger(3).saturationModifier(0.25f)
            .statusEffect(new StatusEffectInstance(StatusEffects.LUCK,200),0.25f).build();
    public static final FoodComponent BREAD_SWORD=
            new FoodComponent.Builder().hunger(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodComponent BREAD_SWORD_HOT=
            new FoodComponent.Builder().hunger(3).saturationModifier(0.25f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,200),0.25f).alwaysEdible().build();
    public static final FoodComponent BREAD_SWORD_VERY_HOT=
            new FoodComponent.Builder().hunger(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodComponent POISON_SWORD=
            new FoodComponent.Builder().hunger(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodComponent STARGAZY_PIE=new FoodComponent.Builder().hunger(3).saturationModifier(0.25f)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON,200),1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,400),1f).alwaysEdible().build();
    public static final FoodComponent MILK_FLESH=
            new FoodComponent.Builder().hunger(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodComponent CHEESE_BERGER=new FoodComponent.Builder().hunger(10).saturationModifier(0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER,200),0.5f).build();
    public static final FoodComponent VILLAGER_ITEM = new FoodComponent.Builder().hunger(10).saturationModifier(0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER,200),1f).alwaysEdible().build();
    public static final FoodComponent IRON_GOLEM_ITEM = new FoodComponent.Builder().hunger(3).saturationModifier(0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE,20),1f).alwaysEdible().build();
}
