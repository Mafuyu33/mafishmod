package net.mafuyu33.mafishmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class CursedLv3 extends Enchantment {


    protected CursedLv3(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean isCursed() {
        return true;
    }
}
