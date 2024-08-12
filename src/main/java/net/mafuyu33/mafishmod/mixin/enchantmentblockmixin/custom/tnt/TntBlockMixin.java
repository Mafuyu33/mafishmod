package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.tnt;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin extends Block {
	public TntBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {}

	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean redirectSetBlockStateOnUse(World world, BlockPos pos, BlockState state, int flags) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,pos);
		if (k == 0) {
			return world.setBlockState(pos, state, flags);
		}
		return true;
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */

	@Inject(method = "onDestroyedByExplosion", at = @At("HEAD"))
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, CallbackInfo ci) {
		if (!world.isClient) {
			int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,pos);
			if(k>0){
				NbtList enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(pos); // 获取物品栈上的附魔信息列表
				BlockEnchantmentStorage.addBlockEnchantment(pos,enchantments);// 将附魔信息列表存储
				world.setBlockState(pos, Blocks.TNT.getDefaultState(), 16);//添加TNT
			}
		}
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */
	@Redirect(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
	public boolean redirectRemoveBlockOnProjectileHit(World world, BlockPos pos, boolean move) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY, pos);
		if(k==0) {
			return world.removeBlock(pos, false);
		}
		return true;
	}
}