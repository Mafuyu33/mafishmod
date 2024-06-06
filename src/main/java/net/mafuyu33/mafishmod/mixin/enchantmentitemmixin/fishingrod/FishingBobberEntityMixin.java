package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.fishingrod;

import com.llamalad7.mixinextras.sugar.Local;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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

import java.util.*;
import java.util.function.Consumer;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
	@Shadow @Nullable public abstract PlayerEntity getPlayerOwner();
	@Shadow
	protected abstract void tickFishingLogic(BlockPos pos);
	@Shadow
	public abstract int use(ItemStack usedItem);

	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	@Unique
	private int delayTimer = 0;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",ordinal = 0) , method = "use")
	private void init3(ItemStack usedItem, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) double d, @Local(ordinal = 1) double e, @Local(ordinal = 2) double f, @Local ItemEntity itemEntity) {//钓上生物
//		int k = EnchantmentHelper.getLevel(Enchantments.SMITE, usedItem);
//		float amp = 2f;
//		if (k > 0) {
//			LivingEntity livingEntity = EntityType.DROWNED.create(this.getWorld());
//			if (livingEntity != null) {
//				livingEntity.setPosition(this.getX(), this.getY(), this.getZ());
//				livingEntity.setVelocity(d * 0.1*amp, (e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08)*amp, f * 0.1*amp);
//				this.getWorld().spawnEntity(livingEntity);
//				itemEntity.remove(RemovalReason.DISCARDED);
//			}
//		}

		int i = EnchantmentHelper.getLevel(Enchantments.DEPTH_STRIDER, usedItem);
		float amp2 = 2f;
		if (i > 0) {
			// 定义可能生成的生物类型列表
			List<EntityType<? extends Entity>> possibleEntities = Arrays.asList(
					EntityType.ZOMBIE,
					EntityType.SKELETON,
					EntityType.SPIDER,
					EntityType.CREEPER,
					EntityType.ENDERMAN,
					EntityType.WITCH,
					EntityType.SLIME,
					EntityType.DROWNED,
					EntityType.HUSK,
					EntityType.TNT,
					EntityType.ARROW,
					EntityType.BAT,
					EntityType.BEE,
					EntityType.BLAZE,
					EntityType.BOAT,
					EntityType.CAVE_SPIDER,
					EntityType.CHICKEN,
					EntityType.COW,
					EntityType.DOLPHIN,
					EntityType.DONKEY,
					EntityType.DRAGON_FIREBALL,
					EntityType.EGG,
					EntityType.ENDER_DRAGON,
					EntityType.ENDER_PEARL,
					EntityType.ENDERMITE,
					EntityType.EVOKER,
					EntityType.EVOKER_FANGS,
					EntityType.EXPERIENCE_ORB,
					EntityType.FIREWORK_ROCKET,
					EntityType.FOX,
					EntityType.GHAST,
					EntityType.GOAT,
					EntityType.GUARDIAN,
					EntityType.HORSE,
					EntityType.IRON_GOLEM,
					EntityType.ITEM,
					EntityType.LLAMA,
					EntityType.MAGMA_CUBE,
					EntityType.MINECART,
					EntityType.MOOSHROOM,
					EntityType.MULE,
					EntityType.OCELOT,
					EntityType.PANDA,
					EntityType.PARROT,
					EntityType.PHANTOM,
					EntityType.PIG,
					EntityType.PIGLIN,
					EntityType.PILLAGER,
					EntityType.PLAYER,
					EntityType.POLAR_BEAR,
					EntityType.RABBIT,
					EntityType.SHEEP,
					EntityType.SHULKER,
					EntityType.SILVERFISH,
					EntityType.SKELETON_HORSE,
					EntityType.SLIME,
					EntityType.SNOW_GOLEM,
					EntityType.SPIDER,
					EntityType.SQUID,
					EntityType.STRAY,
					EntityType.TNT,
					EntityType.TNT_MINECART,
					EntityType.TRADER_LLAMA,
					EntityType.TROPICAL_FISH,
					EntityType.TURTLE,
					EntityType.VILLAGER,
					EntityType.WITCH,
					EntityType.WITHER,
					EntityType.WITHER_SKELETON,
					EntityType.WOLF,
					EntityType.ZOMBIE,
					EntityType.ZOMBIE_VILLAGER,
					EntityType.ZOMBIFIED_PIGLIN
					// Add other entities as needed
			);

			// 从列表中随机选择一个生物类型
			Random random = new Random();

			EntityType<? extends Entity> randomEntityType = possibleEntities.get(random.nextInt(possibleEntities.size()));

			Entity livingEntity =  randomEntityType.create(this.getWorld());
			if (livingEntity != null) {
				livingEntity.setPosition(this.getX(), this.getY(), this.getZ());
				livingEntity.setVelocity(d * 0.1*amp2, (e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08)*amp2, f * 0.1*amp2);
				this.getWorld().spawnEntity(livingEntity);
				itemEntity.remove(RemovalReason.DISCARDED);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)V",ordinal = 1), method = "tickFishingLogic")
	private void init0(CallbackInfo info) {//自动钓鱼
		// 添加自动收回鱼漂逻辑
		PlayerEntity playerEntity = getPlayerOwner();
		if (playerEntity!=null) {
			ItemStack mainHandStack = playerEntity.getMainHandStack();
			ItemStack offHandStack = playerEntity.getOffHandStack();
			if (mainHandStack.getItem() == Items.FISHING_ROD || offHandStack.getItem()==Items.FISHING_ROD) {
				ItemStack itemStack = mainHandStack.getItem() == Items.FISHING_ROD ? mainHandStack : offHandStack;
				int k =EnchantmentHelper.getLevel(Enchantments.LOYALTY, itemStack);
				if (k > 0) {
					System.out.println("use");
					this.use(playerEntity.getMainHandStack());
				}
			}
		}
	}

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
}