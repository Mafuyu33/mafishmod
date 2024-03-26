package net.mafuyu33.mafishmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class NormalLv5 extends Enchantment {

    private static final int BASE_POWERS = 5;
    private static final int POWERS_PER_LEVEL = 11;
    private static final int MIN_MAX_POWER_DIFFERENCES = 20;

    protected NormalLv5(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }

    public int getMinPower(int level) {
        return BASE_POWERS + (level - 1) * POWERS_PER_LEVEL;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + MIN_MAX_POWER_DIFFERENCES;
    }
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
