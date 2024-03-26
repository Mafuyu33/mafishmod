package net.mafuyu33.mafishmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Enchantment.class)
public class AlwaysEnchantableMixin {
	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * Always Enchantable.
	 */
	@Overwrite
	public boolean isAcceptableItem(ItemStack stack) {
		return true;
	}
}