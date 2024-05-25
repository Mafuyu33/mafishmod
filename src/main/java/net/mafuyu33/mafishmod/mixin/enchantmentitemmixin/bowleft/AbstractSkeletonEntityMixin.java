package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.bowleft;

import com.llamalad7.mixinextras.sugar.Local;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements RangedAttackMob {
	protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "shootAt")
	private void init(LivingEntity target, float pullProgress, CallbackInfo ci, @Local(ordinal = 0) double d , @Local(ordinal = 1) double e
			, @Local(ordinal = 2) double f ,  @Local(ordinal = 3) double g, @Local(ordinal = 0) PersistentProjectileEntity persistentProjectileEntity) {
		ItemStack bow = this.getStackInHand(this.getActiveHand());
		int i = EnchantmentHelper.getLevel(ModEnchantments.BOW_LEFT,bow);
		System.out.println(i);
		if(i>0) {
			// 左偏移的量，可以调整这个值来改变偏移的程度
			double offset = -1.0; // 偏移量，正数表示左偏移，负数表示右偏移
			// 计算偏移方向
			double offsetX = -f * offset / g;
			double offsetZ = d * offset / g;
			// 修改速度向量
			persistentProjectileEntity.setVelocity(d + offsetX, e + g * 0.20000000298023224, f + offsetZ, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
		}
	}
}