package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin extends Item implements Vanishable {
	@Unique
	public ArrayList<FishingBobberEntity> fishHooks;
	public FishingRodItemMixin(Settings settings) {
		super(settings);
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	private void injected(CallbackInfo ci) {
		this.fishHooks = new ArrayList<>();
	}

	/**
	 * @author
	 * Mafish33
	 * @reason
	 * 多重射击等附魔
	 */
	@Overwrite
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (user.fishHook != null) {//冲击
			int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, itemStack);//冲击附魔
			if (k > 0) {
				Vec3d fishHookPos = user.fishHook.getPos();
				Vec3d userPos = user.getPos();
				// 计算朝向鱼钩的方向向量
				Vec3d direction = fishHookPos.subtract(userPos).normalize();
				// 设定速度大小（可以根据需要调整）
				double speed = 1 + k * 0.5;
				// 计算最终的速度向量
				Vec3d velocity = direction.multiply(speed);
				// 将速度应用到玩家身上
				user.addVelocity(velocity);
			}
		}
		int m = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, itemStack);//多重射击附魔
		int sum = 0;
		if (user.fishHook != null) {//收杆
			if (!world.isClient) {
				if(m > 0){//拉鱼杆
					for(int k = 0; k < m + 2 ; k++){
						int i = fishHooks.get(k).use(itemStack);
						sum = sum + i;
					}
				}else {
					sum = user.fishHook.use(itemStack);
				}
				itemStack.damage(sum, user, p -> p.sendToolBreakStatus(hand));
			}
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
			user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
		} else {//抛竿
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
			if (!world.isClient) {
				int i = EnchantmentHelper.getLure(itemStack);
				int j = EnchantmentHelper.getLuckOfTheSea(itemStack);

				if(m > 0){
					for(int k = 0; k < m + 2; k++){//抛出m+2个鱼钩，存放在1至m中
						// 生成随机偏移速度
						double offsetX = world.random.nextGaussian() * 0.1;
						double offsetY = world.random.nextGaussian() * 0.1;
						double offsetZ = world.random.nextGaussian() * 0.1;
						// 创建带有随机偏移速度的鱼竿实体
						FishingBobberEntity bobber = new FishingBobberEntity(user, world, j, i);
						bobber.addVelocity(offsetX, offsetY, offsetZ);

						// 将鱼竿实体添加到列表并在世界中生成
						fishHooks.add(k, bobber);
						world.spawnEntity(bobber);

						System.out.println(fishHooks.get(k));
					}
				}else {
					world.spawnEntity(new FishingBobberEntity(user, world, j, i));
				}
			}
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
		}
		return TypedActionResult.success(itemStack, world.isClient());
	}


//	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "use")
//	private void init1(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {//抛竿
//		ItemStack itemStack = user.getStackInHand(hand);
//		int m = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, itemStack);//多重射击附魔
//		int i = EnchantmentHelper.getLure(itemStack);
//		int j = EnchantmentHelper.getLuckOfTheSea(itemStack);
//		if(m>0){
//			fishHooks.add(0, );
//			for(int k = 1; k < m + 1; k++){
//				fishHooks.add(k, new FishingBobberEntity(user, world, j, i));
//				world.spawnEntity(fishHooks.get(k));
//				System.out.println(fishHooks.get(k));
//			}
//		}
//	}
//	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;use(Lnet/minecraft/item/ItemStack;)I"), method = "use")
//	private void init2(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {//拉杆
//		ItemStack itemStack = user.getStackInHand(hand);
//		int m = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, itemStack);//多重射击附魔
//		if(m>0){
//			for(int k = 0; k < m + 1; k++){
//				System.out.println(user.fishHook);
//				if(user.fishHook!=null) {
//					fishHooks.get(k).use(itemStack);
//				}
//			}
//		}
//	}

}