package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.targetblock;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.TargetBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetBlock.class)
public abstract class TargetBlockMixin {
	@Inject(at = @At("HEAD"), method = "onProjectileHit",cancellable = true)
	private void init(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile, CallbackInfo ci) {

		int k = BlockEnchantmentStorage.getLevel(Enchantments.PROJECTILE_PROTECTION,hit.getBlockPos());
		if(k>0 && !world.isClient){
			Entity entity = projectile.getOwner();
			if(entity instanceof LivingEntity livingEntity) {
				double d = livingEntity.getX() - projectile.getX();
				double e = livingEntity.getY() - projectile.getY();
				double f = livingEntity.getZ() - projectile.getZ();
			// 创建物品实体并设置位置
			ProjectileEntity newProjectileEntity = new ArrowEntity(world,  projectile.getX(), projectile.getY(), projectile.getZ() ,new ItemStack(Items.ARROW));
			newProjectileEntity.setVelocity(d * 0.1*1.3, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08*1.3, f * 0.1*1.3);

			// 将物品实体添加到世界中
			projectile.discard();
			world.spawnEntity(newProjectileEntity);
			}else {
				ProjectileEntity newProjectileEntity = new ArrowEntity(world, projectile.getX(), projectile.getY(), projectile.getZ(),new ItemStack(Items.ARROW));
				newProjectileEntity.setPosition(projectile.getX(), projectile.getY(), projectile.getZ());
				newProjectileEntity.setVelocity(0,0,0);
				// 将物品实体添加到世界中
				projectile.discard();
				world.spawnEntity(newProjectileEntity);
			}
			ci.cancel();
		}
	}
}