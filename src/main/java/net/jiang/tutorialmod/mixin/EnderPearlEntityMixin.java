package net.jiang.tutorialmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin extends ThrownItemEntity {
	public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
		super(entityType, d, e, f, world);
	}

	public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
		super(entityType, livingEntity, world);
	}

	@Override
	public Item getDefaultItem() {
		return Items.ENDER_PEARL;
	}

	@Inject(at = @At("HEAD"), method = "onCollision")
	private void init(HitResult hitResult, CallbackInfo ci){
		PlayerEntity playerEntity = ((PlayerEntity) this.getOwner());
		if (playerEntity != null) {
			Hand hand = playerEntity.getActiveHand();
			if (hand != null) {
				ItemStack itemStack = playerEntity.getStackInHand(hand);
				if (itemStack.getItem() == Items.ENDER_PEARL) {
					int k = EnchantmentHelper.getLevel(Enchantments.CHANNELING, itemStack);
					if (k > 0) {
						BlockPos blockPos = this.getBlockPos();
						LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
						if (lightningEntity != null) {
							lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
							lightningEntity.setChanneler(playerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerEntity : null);
							this.getWorld().spawnEntity(lightningEntity);
							SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
							this.playSound(soundEvent, 5, 1.0F);
						}
					}
				}
			}
		}
	}
}