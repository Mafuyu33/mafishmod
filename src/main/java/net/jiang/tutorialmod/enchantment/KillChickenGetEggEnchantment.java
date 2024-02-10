package net.jiang.tutorialmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class KillChickenGetEggEnchantment extends Enchantment {
    protected KillChickenGetEggEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
