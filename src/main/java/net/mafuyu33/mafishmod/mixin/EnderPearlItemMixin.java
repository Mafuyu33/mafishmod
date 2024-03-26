package net.mafuyu33.mafishmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin extends Item {
	public EnderPearlItemMixin(Settings settings) {
		super(settings);
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * 重写
	 */
	@Overwrite
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW,
				SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

		//快速装填
		int k = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, itemStack);
		if (k <= 0) {
			user.getItemCooldownManager().set(this, 20);
		}

		if (!world.isClient) {
			EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, user);
			enderPearlEntity.setItem(itemStack);
			int n = EnchantmentHelper.getLevel(Enchantments.VANISHING_CURSE, itemStack);
//			int m = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, itemStack);
//			int j = EnchantmentHelper.getLevel(Enchantments., itemStack);

			if( n > 0 ){
				enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
				world.spawnEntity(enderPearlEntity);

				enderPearlEntity.discard();
			}

//			else if( m > 0 ){
//				int numPearlsToThrow = 5; // 设置要丢出的末影珍珠数量
//				for(int i=0; i<numPearlsToThrow; i++){
//					enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), i, 1.5F, i+1);
//					world.spawnEntity(enderPearlEntity);
//				}
//			}
//			else if( j > 0 ){
//				j = 10;
//				for(int i=0; i<j; i++){
//					enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), i, 1.5F, i+1);
//					world.spawnEntity(enderPearlEntity);
//				}
//			}
			else {
				enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
				world.spawnEntity(enderPearlEntity);
			}
		}

		user.incrementStat(Stats.USED.getOrCreateStat(this));

		if (!user.getAbilities().creativeMode & EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStack) < 1) {//无限
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}

}