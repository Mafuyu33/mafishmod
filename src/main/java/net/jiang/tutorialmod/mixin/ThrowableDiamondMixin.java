package net.jiang.tutorialmod.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.jiang.tutorialmod.entity.DiamondProjectileEntity;
import net.jiang.tutorialmod.event.KeyInputHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ThrowableDiamondMixin implements FabricItem{
	@Inject(at = @At("HEAD"), method = "use")
	private void init(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

		ItemStack itemStack = user.getStackInHand(hand);
		if (itemStack.getItem() == Items.DIAMOND) {

			float throwPower = KeyInputHandler.getThrowPower();//获取当前的投掷力度

			world.playSound(null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));

			if (!world.isClient) {
				DiamondProjectileEntity diamondProjectileEntity = new DiamondProjectileEntity(user, world);
				diamondProjectileEntity.setItem(itemStack);
				diamondProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, throwPower, 1.0f);
				world.spawnEntity(diamondProjectileEntity);
			}

			user.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
			if (!user.getAbilities().creativeMode) {
				itemStack.decrement(1);
			}
		}
	}
}

