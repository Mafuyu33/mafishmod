package net.mafuyu33.mafishmod.mixin.itemmixin.fireworkrocket;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin extends Item {

	public FireworkRocketItemMixin(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		float f = 1.0F;
		entity.getWorld().createExplosion(entity, entity.getX(), entity.getBodyY(0.0625), entity.getZ(),
				f, World.ExplosionSourceType.TNT);
		entity.addVelocity(0,10,0);
		stack.decrement(1);
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void init(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		PlayerEntity player = context.getPlayer();
		ItemStack breastplate = player.getInventory().getArmorStack(2);
		if(breastplate.getItem()== Items.ELYTRA){
			player.addVelocity(0,10,0);
		}
	}

}