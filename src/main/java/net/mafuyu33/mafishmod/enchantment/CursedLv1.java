package net.mafuyu33.mafishmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class CursedLv1 extends Enchantment {


    protected CursedLv1(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean isCursed() {
        return true;
    }
}
