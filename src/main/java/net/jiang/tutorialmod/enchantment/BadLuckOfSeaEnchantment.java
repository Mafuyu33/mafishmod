package net.jiang.tutorialmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class BadLuckOfSeaEnchantment extends Enchantment {
    protected BadLuckOfSeaEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    public boolean isCursed() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
