package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.slimefeet;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(at = @At("HEAD"), method = "onLandedUpon",cancellable = true)
	private void init(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
		Iterable<ItemStack> armorItems = entity.getArmorItems();
		for (ItemStack armorItem : armorItems) {
			if (armorItem.getItem() instanceof ArmorItem && ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.BOOTS) {
				int k = EnchantmentHelper.getLevel(ModEnchantments.STICKY, armorItem);//击退
				if (k > 0) {
					ci.cancel();
					break;
				}
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onEntityLand", cancellable = true)
	private void init1(BlockView world, Entity entity, CallbackInfo ci) {
		Iterable<ItemStack> armorItems = entity.getArmorItems();
		for (ItemStack armorItem : armorItems) {
			if (armorItem.getItem() instanceof ArmorItem && ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.BOOTS) {
				int k = EnchantmentHelper.getLevel(ModEnchantments.STICKY, armorItem);//击退
				if (k > 0) {
					mafishmod$bounce(entity);
					ci.cancel();
					break;
				}
			}
		}
	}
	@Unique
	private void mafishmod$bounce(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		if (vec3d.y < 0.0) {
			double d = entity instanceof LivingEntity ? 1.25 : 0.8;
			entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
		}
	}
}