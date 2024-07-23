package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.multishot_trident;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "onStoppedUsing")
	private void init(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		int k = EnchantmentHelper.getLevel(Enchantments.MULTISHOT,stack);
		if(k>0){
			for(int i = 0; i < k + 1; i++){//抛出m+2个鱼钩，存放在1至m中
				// 生成随机偏移速度
				double offsetX = world.random.nextGaussian() * 0.1;
				double offsetY = world.random.nextGaussian() * 0.1;
				double offsetZ = world.random.nextGaussian() * 0.1;
				// 创建带有随机偏移速度的鱼竿实体
				int j = EnchantmentHelper.getRiptide(stack);
				PlayerEntity playerEntity = ((PlayerEntity) user);
				TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
				tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
				tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
				tridentEntity.addVelocity(offsetX, offsetY, offsetZ);
				world.spawnEntity(tridentEntity);
			}

		}
	}
}