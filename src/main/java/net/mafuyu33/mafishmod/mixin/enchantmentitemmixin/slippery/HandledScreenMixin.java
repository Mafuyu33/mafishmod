package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.slippery;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin{
	@Shadow @Nullable
	protected Slot focusedSlot;

	@Inject(at = @At(value = "HEAD"), method = "handleHotbarKeyPressed",cancellable = true)
	private void init(int keyCode, int scanCode, CallbackInfoReturnable<Boolean> cir){
		Slot slot1 = this.focusedSlot;
		if(slot1 != null) {
			ItemStack itemStack = slot1.getStack();
			if (EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY, itemStack) > 0) {
				cir.cancel();
			}
		}
	}
}