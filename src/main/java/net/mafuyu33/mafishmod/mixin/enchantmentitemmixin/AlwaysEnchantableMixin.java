package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin;

import net.mafuyu33.mafishmod.util.ConfigHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class AlwaysEnchantableMixin {

	@Final
	@Shadow
	public EnchantmentTarget target;

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * Always Enchantable.
	 */
	@Overwrite
	public boolean isAcceptableItem(ItemStack stack) {
		if(ConfigHelper.isAlwaysEnchantable()) {
			return true;
		}else {
			return this.target.isAcceptableItem(stack.getItem());
		}
	}
}