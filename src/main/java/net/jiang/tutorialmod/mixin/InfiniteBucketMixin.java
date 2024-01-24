package net.jiang.tutorialmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
            player.sendMessage(Text.of("a: " + a));
            if (!player.getAbilities().creativeMode & a != 1) {
                return new ItemStack(Items.BUCKET);
            } else {
                return stack;
            }
        }
    }
}
