package net.mafuyu33.mafishmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class CursedTreasureLv1 extends Enchantment {
    protected CursedTreasureLv1(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }

    public boolean isTreasure() {
        return true;
    }
    public int getMaxLevel() {
        return 1;
    }

    public boolean isCursed() {
        return true;
    }
}

