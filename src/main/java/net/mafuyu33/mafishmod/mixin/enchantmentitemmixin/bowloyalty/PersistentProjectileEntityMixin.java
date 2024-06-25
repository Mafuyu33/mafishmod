package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.bowloyalty;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
	public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow public abstract ItemStack getItemStack();
	@Shadow protected boolean inGround;

	@Shadow public abstract void setNoClip(boolean noClip);

	@Shadow public abstract boolean isNoClip();

	@Shadow public PersistentProjectileEntity.PickupPermission pickupType;

	@Shadow protected abstract ItemStack asItemStack();

	@Unique
	private static TrackedData<Byte> LOYALTY;
	@Unique
	public int returnTimer;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isNoClip()Z"), method = "tick")
	private void init(CallbackInfo ci) {

		Entity entity = this.getOwner();
		int loyaltyLevel = getLoyaltyFromBow(entity);
		if (loyaltyLevel > 0 && this.dataTracker.get(LOYALTY)==0) {
			this.dataTracker.set(LOYALTY, (byte) loyaltyLevel);
		}
		loyaltyLevel = this.dataTracker.get(LOYALTY);
		if (loyaltyLevel > 0 && (this.inGround || this.isNoClip()) && entity != null) {
			if (!this.isOwnerAlive()) {
				if (!this.getWorld().isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
					this.dropStack(this.asItemStack(), 0.1F);
				}
				this.discard();
			} else {
				this.setNoClip(true);
				Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
				this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)loyaltyLevel, this.getZ());
				if (this.getWorld().isClient) {
					this.lastRenderY = this.getY();
				}

				double d = 0.05 * (double)loyaltyLevel;
				this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
				if (this.returnTimer == 0) {
					this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
				}

				++this.returnTimer;
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void init1(CallbackInfo ci) {
		LOYALTY = DataTracker.registerData(PersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
	}

	@Inject(at = @At("TAIL"), method = "initDataTracker")
	private void init2(CallbackInfo ci) {
		this.dataTracker.startTracking(LOYALTY, (byte)0);
	}
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)V")
	private void init3(EntityType type, LivingEntity owner, World world, ItemStack stack, CallbackInfo ci) {
		this.dataTracker.set(LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
	}
	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	private void init4(NbtCompound nbt, CallbackInfo ci) {
		this.dataTracker.set(LOYALTY, (byte)EnchantmentHelper.getLoyalty(getItemStack()));
	}
	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	private void init5(NbtCompound nbt, CallbackInfo ci) {
		nbt.putByte("Loyalty", this.dataTracker.get(LOYALTY));
	}

	@Unique
	private int getLoyaltyFromBow(Entity entity) {
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			ItemStack mainHandItem = livingEntity.getStackInHand(Hand.MAIN_HAND);
			ItemStack offHandItem = livingEntity.getStackInHand(Hand.OFF_HAND);

			if (mainHandItem.getItem() == Items.BOW) {
				return EnchantmentHelper.getLoyalty(mainHandItem);
			}

			if (offHandItem.getItem() == Items.BOW) {
				return EnchantmentHelper.getLoyalty(offHandItem);
			}
		}
		return 0;
	}
	@Unique
	private boolean isOwnerAlive() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
		} else {
			return false;
		}
	}
}