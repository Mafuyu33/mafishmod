package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.fishingrod;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
	@Shadow @Nullable public abstract PlayerEntity getPlayerOwner();
	@Shadow
	protected abstract void tickFishingLogic(BlockPos pos);

	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	@Unique
	private int delayTimer = 0;

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {//冰霜行者
		PlayerEntity playerEntity = getPlayerOwner();
		if (playerEntity!=null) {
			Hand hand = playerEntity.getActiveHand();
			if (hand != null) {
				ItemStack itemStack = playerEntity.getStackInHand(hand);
				if (itemStack.getItem() == Items.FISHING_ROD) {
					int k = EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, itemStack);
				if (k > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					FluidState fluidState = world.getFluidState(blockPos);
					if (fluidState.isIn(FluidTags.WATER)) {
						this.addVelocity(0,0.3,0);
						if (delayTimer < 1) {
							delayTimer++;
						} else {
							world.setBlockState(blockPos, Blocks.FROSTED_ICE.getDefaultState(), 3);
							delayTimer = 0;
						}
					}
				}
				}
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "tick")
	private void init1(CallbackInfo info) {//海之厌恶
		PlayerEntity playerEntity = getPlayerOwner();
		if (playerEntity!=null) {
			Hand hand = playerEntity.getActiveHand();
			if (hand != null) {
				ItemStack itemStack = playerEntity.getStackInHand(hand);
				if (itemStack.getItem() == Items.FISHING_ROD) {
					int k = EnchantmentHelper.getLevel(ModEnchantments.BAD_LUCK_OF_SEA, itemStack);
					if (k > 0) {
						World world = this.getWorld();
						BlockPos blockPos = this.getBlockPos();
						FluidState fluidState = world.getFluidState(blockPos);
						if (fluidState.isIn(FluidTags.WATER)) {
							this.addVelocity(0, 1, 0);
						}
					}
				}
			}
		}
	}
	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * 添加了力量附魔
	 */
	@Overwrite
	public void pullHookedEntity(Entity entity) {//力量
		float power_enchantment_level = 1;
		Entity entity2 = this.getOwner();
		if (entity2 instanceof PlayerEntity) {
			ItemStack itemStack = ((PlayerEntity) entity2).getMainHandStack();
			int k = EnchantmentHelper.getLevel(Enchantments.POWER, itemStack);
			if (k > 0) {
				power_enchantment_level = 1.0f + k * 0.5f;
			}
		}
		if (entity2 != null) {
			Vec3d vec3d = (new Vec3d(entity2.getX() - this.getX(), entity2.getY() - this.getY(), entity2.getZ() - this.getZ())).multiply(0.1*power_enchantment_level);
			entity.setVelocity(entity.getVelocity().add(vec3d));
		}
	}
    @Shadow private FishingBobberEntity.State state;


	@Inject(at = @At("TAIL"), method = "tick")
	private void init2(CallbackInfo info) {//火焰附加，能在岩浆里钓鱼
		float f = 0.0F;
		PlayerEntity playerEntity = getPlayerOwner();
		if (playerEntity != null) {
			ItemStack itemStack = playerEntity.getMainHandStack();
			if(itemStack.getItem() == Items.FISHING_ROD) {
				int k = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, itemStack);
				if (k > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					FluidState fluidState = world.getFluidState(blockPos);
					if (fluidState.isIn(FluidTags.LAVA)) {
						f = fluidState.getHeight(this.getWorld(), blockPos);
					}
					boolean bl = f > 0.0F;

					if (bl) {
//						this.addVelocity(0,0.03,0);
						this.setVelocity(0, 0.1, 0);
						this.state = FishingBobberEntity.State.BOBBING;

						if (!this.getWorld().isClient) {
							this.tickFishingLogic(blockPos);
						}
					}
				}
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "use")
	private void init3(ItemStack usedItem, CallbackInfoReturnable<Integer> cir){
		PlayerEntity playerEntity = getEntityWorld().getClosestPlayer(this,40);
	}
}