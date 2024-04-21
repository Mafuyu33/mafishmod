package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.tnt;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin extends Block {
	public TntBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */
	@Overwrite
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,pos);
		ItemStack itemStack = player.getStackInHand(hand);
		if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
			return super.onUse(state, world, pos, player, hand, hit);
		} else {
			primeTnt(world, pos, player);
			if(k==0) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);//删除TNT
			}
			Item item = itemStack.getItem();
			if (!player.isCreative()) {
				if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
					itemStack.damage(1, player, (playerx) -> {
						playerx.sendToolBreakStatus(hand);
					});
				} else {
					itemStack.decrement(1);
				}
			}

			player.incrementStat(Stats.USED.getOrCreateStat(item));
			return ActionResult.success(world.isClient);
		}
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */

	@Overwrite
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		if (!world.isClient) {
			int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,pos);
			if(k>0){
				NbtList enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(pos); // 获取物品栈上的附魔信息列表
				BlockEnchantmentStorage.addBlockEnchantment(pos,enchantments);// 将附魔信息列表存储
				world.setBlockState(pos, Blocks.TNT.getDefaultState(), 16);//添加TNT
			}
			TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, explosion.getCausingEntity());
			int i = tntEntity.getFuse();
			tntEntity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
			world.spawnEntity(tntEntity);
		}
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */
	@Overwrite
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (!world.isClient) {
			BlockPos blockPos = hit.getBlockPos();
			int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,blockPos);
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire() && projectile.canModifyAt(world, blockPos)) {
				primeTnt(world, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
				if(k==0) {
					world.removeBlock(blockPos, false);
				}
			}
		}

	}
}