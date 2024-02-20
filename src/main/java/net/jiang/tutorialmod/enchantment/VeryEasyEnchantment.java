package net.jiang.tutorialmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class VeryEasyEnchantment extends Enchantment {
    protected VeryEasyEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }


    public int getMaxLevel() {
        return 5;
    }

    public boolean isCursed() {
        return true;
    }
}
