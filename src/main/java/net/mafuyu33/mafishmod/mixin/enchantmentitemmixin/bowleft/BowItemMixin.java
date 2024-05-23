package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.bowleft;

import com.llamalad7.mixinextras.sugar.Local;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.item.BowItem.getPullProgress;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem implements Vanishable {
	public BowItemMixin(Settings settings) {
		super(settings);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "onStoppedUsing")
	private void init(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local(ordinal = 0) PersistentProjectileEntity persistentProjectileEntity) {
		int o = EnchantmentHelper.getLevel(ModEnchantments.BOW_LEFT, stack);
		if(user instanceof PlayerEntity playerEntity && o>0) {
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			float f = getPullProgress(i);
			float leftOffset = i * 1.0F; // 偏移角度（度）
			float newYaw = playerEntity.getYaw() - leftOffset; // 调整后的偏航角
			persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), newYaw, 0.0F, f * 3.0F, 1.0F);
		}
	}
}