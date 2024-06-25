package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.bowloyalty;

import com.llamalad7.mixinextras.sugar.Local;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem implements Vanishable {
	public BowItemMixin(Settings settings) {
		super(settings);
	}

//	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "onStoppedUsing")
//	private void init(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local(ordinal = 0) PersistentProjectileEntity persistentProjectileEntity) {
//		int o = EnchantmentHelper.getLevel(Enchantments.LOYALTY, stack);
//		if(o>0) {
//			// Set loyalty level
//
//			persistentProjectileEntity.getDataTracker().set(LOYALTY, (byte)EnchantmentHelper.getLoyalty(stack));
//		}
//	}
}