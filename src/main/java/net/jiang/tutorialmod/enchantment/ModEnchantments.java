package net.jiang.tutorialmod.enchantment;

import net.jiang.tutorialmod.TutorialMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static Enchantment BAD_LUCK_OF_SEA = register("bad_luck_of_sea",
            new BadLuckofSeaEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND));

    public static Enchantment EIGHT_GODS_PASS_SEA = register("eight_gods_pass_sea",
            new EightGodsPassSeaEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET));

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(TutorialMod.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments(){
        System.out.println("注册附魔");
    }
}
