package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin extends Item implements Vanishable {
	public FishingRodItemMixin(Settings settings) {
		super(settings);
	}
	@Inject(at = @At("HEAD"), method = "use")
	private void init(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (user.fishHook != null) {
			int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, itemStack);//冲击附魔
			if (k > 0) {
//				//发送信息给玩家
//				user.sendMessage(Text.literal((String.valueOf(k))),false);

				Vec3d fishHookPos = user.fishHook.getPos();
				Vec3d userPos = user.getPos();

				// 计算朝向鱼钩的方向向量
				Vec3d direction = fishHookPos.subtract(userPos).normalize();

				// 设定速度大小（可以根据需要调整）
				double speed = 1 + k * 0.5;

				// 计算最终的速度向量
				Vec3d velocity = direction.multiply(speed);
//				//发送信息给玩家
//				user.sendMessage(Text.literal((String.valueOf(velocity))),false);

				// 将速度应用到玩家身上
				user.addVelocity(velocity);


			}
		}
	}

}