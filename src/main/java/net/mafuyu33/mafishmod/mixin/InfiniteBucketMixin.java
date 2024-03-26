package net.mafuyu33.mafishmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

public class InfiniteBucketMixin {
    @Mixin(BucketItem.class)
    public abstract static class InFiniteBucketMixin extends Item implements FluidModificationItem {
        public InFiniteBucketMixin(Settings settings) {
            super(settings);
        }

        /**
         * @author
         * Mafuyu33
         * @reason
         * Add infinite bucket
         */
        @Overwrite
        public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
            int a = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack);//无限
            if (!player.getAbilities().creativeMode & a != 1) {
                return new ItemStack(Items.BUCKET);
            } else {
                return stack;
            }
        }
    }
}
