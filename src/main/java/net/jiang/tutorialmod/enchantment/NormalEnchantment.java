package net.jiang.tutorialmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class NormalEnchantment extends Enchantment {
    protected NormalEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
