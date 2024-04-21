package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.slippery;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class UnPickUpAbleItemMixin {
	@Shadow public abstract void setPickupDelayInfinite();

	@Shadow public abstract ItemStack getStack();

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
//		if(EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY,this.getStack())>0) {
//			this.setPickupDelayInfinite();
//		}
	}
}