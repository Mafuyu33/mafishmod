package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class ArmorEnchantmentMixin extends Entity implements Attackable {
	@Shadow public abstract Iterable<ItemStack> getArmorItems();

	@Shadow public abstract boolean hurtByWater();

	@Shadow public abstract void onDamaged(DamageSource damageSource);

	@Shadow @Nullable public abstract DamageSource getRecentDamageSource();

	@Shadow public abstract void kill();

	protected ArmorEnchantmentMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique
	private Random getRandom() {
		return this.random;
	}


	@Inject(at = @At("HEAD"), method = "onKilledBy")
	private void init(LivingEntity adversary, CallbackInfo info) {
		BlockPos blockPos =this.getBlockPos();
		World world =this.getWorld();
		if (adversary != null) {
			if (this.getType() == EntityType.HORSE){
				Iterable<ItemStack> armorItems = this.getArmorItems();
				for (ItemStack armorItem : armorItems) {
					int j = EnchantmentHelper.getLevel(ModEnchantments.KILL_MY_HORSE, armorItem);//敢杀我的马！
					int k = EnchantmentHelper.getLevel(ModEnchantments.KILL_MY_HORSE_PLUS, armorItem);//敢杀我的马！plus
					if (j>0) {
						EntityType.WARDEN.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
					}
					if (k>0) {
						EntityType.WARDEN.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.BLAZE.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.CREEPER.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.EVOKER.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.GHAST.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.HOGLIN.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.HUSK.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.MAGMA_CUBE.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.PHANTOM.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.PIGLIN.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.RAVAGER.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.SHULKER.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.SILVERFISH.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.SKELETON.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.SLIME.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.STRAY.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.VEX.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.VINDICATOR.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.WITCH.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.WITHER_SKELETON.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.ZOGLIN.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.ZOMBIE.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
						EntityType.ZOMBIE_VILLAGER.spawn(((ServerWorld) world), blockPos, SpawnReason.TRIGGERED);
					}
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init1(CallbackInfo info) {
		Iterable<ItemStack> armorItems = this.getArmorItems();

		if (this.getType() == EntityType.HORSE){
			for (ItemStack armorItem : armorItems) {//检测马铠
				int k = EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, armorItem);
				int j = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, armorItem);
				int i = EnchantmentHelper.getLevel(Enchantments.CHANNELING, armorItem);
				if (k>0) {//冰霜行者
					freezeWater(this, getWorld(), this.getBlockPos(), k+1);
				}
				if (j>0) {//火焰附加
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					world.setBlockState(blockPos, Blocks.FIRE.getDefaultState(), 3);
				}
				if (i>0) {//引雷
					BlockPos blockPos = this.getBlockPos();
					LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
					if (lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
						this.getWorld().spawnEntity(lightningEntity);
						SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
						this.playSound(soundEvent, 5, 1.0F);
					}
				}
			}
		}


		for (ItemStack armorItem : armorItems) {
			if (armorItem.getItem() instanceof ArmorItem && ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.BOOTS) {//鞋子
				int k = EnchantmentHelper.getLevel(ModEnchantments.BAD_LUCK_OF_SEA, armorItem);//海之嫌弃
				if (k > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					FluidState fluidState = world.getFluidState(blockPos);
					if (fluidState.isIn(FluidTags.WATER)) {

						BlockPos closestNonLiquidBlockPos = null;
						double closestDistanceSq = Double.MAX_VALUE; // 初始设置为最大值

						for (int xOffset = -20; xOffset <= 19; xOffset++) {
							for (int zOffset = -20; zOffset <= 19; zOffset++) {
								BlockPos currentPos = blockPos.add(xOffset, 0, zOffset);
								FluidState fluidState1 = world.getFluidState(currentPos);

								// 检查当前方块是否不是液体方块
								if (!fluidState1.isIn(FluidTags.WATER)) {
									// 计算当前方块与玩家的距离的平方
									double distanceSq = this.squaredDistanceTo(Vec3d.ofCenter(currentPos));

									// 如果当前方块更近，则更新最近的非液体方块信息
									if (distanceSq < closestDistanceSq) {
										closestDistanceSq = distanceSq;
										closestNonLiquidBlockPos = currentPos;
									}
								}
							}
						}

						if (closestNonLiquidBlockPos != null) {
							// 发送信息给玩家
//							this.sendMessage(Text.literal("检测到最近的固体方块可弹射"));

							// 计算方向向量
							Vec3d direction = Vec3d.ofCenter(closestNonLiquidBlockPos).subtract(this.getPos()).normalize();

							double speed = 1; // 设定速度大小（可以根据需要调整）

							// 计算最终的速度向量
							Vec3d velocity = direction.multiply(speed);

							this.addVelocity(0, 1, 0);
							this.addVelocity(velocity); // 应用速度
						}


						else {
							this.addVelocity(0, 1, 0);
						}
					}
				}


				int j = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, armorItem);//火焰附加
				if (j > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					world.setBlockState(blockPos, Blocks.FIRE.getDefaultState(), 3);
				}

				int i = EnchantmentHelper.getLevel(Enchantments.CHANNELING, armorItem);//引雷
				if (i > 0) {
					BlockPos blockPos = this.getBlockPos();
					LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
					if (lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
						this.getWorld().spawnEntity(lightningEntity);
						SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
						this.playSound(soundEvent, 5, 1.0F);
					}
				}

				int m = EnchantmentHelper.getLevel(ModEnchantments.EIGHT_GODS_PASS_SEA, armorItem);//八仙过海
				if (m > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					checkAndReplaceWaterBlocks(world, blockPos);
				}
//				int n = EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY, armorItem);//肥皂
//				if (n > 0) {
//
//				}
			}
			if (getWorld().isClient && armorItem.getItem() instanceof ArmorItem
					&& ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.HELMET) {//帽子
				int o = EnchantmentHelper.getLevel(ModEnchantments.MUTE, armorItem);//静音
				if (o > 0 && this.isPlayer()) {
					mute();
				}
			}
			if (armorItem.getItem() instanceof ArmorItem) {//随便什么装甲
				int p = EnchantmentHelper.getLevel(ModEnchantments.NO_BLAST_PROTECTION, armorItem);//爆炸不保护
				if (p > 0 && this.getRecentDamageSource()!= null){
					if(this.getRecentDamageSource().getName().contains("explosion")) {
						this.kill();
					}
				}
			}
		}
	}

	@Unique
	@Environment(EnvType.CLIENT)
	private void mute(){
		GameOptions gameOptions = MinecraftClient.getInstance().options;
		gameOptions.getSoundVolumeOption(SoundCategory.MASTER).setValue((double) 0);
	}
	@Unique
	private static void freezeWater(ArmorEnchantmentMixin entity, World world, BlockPos blockPos, int level) {
		if (entity.isOnGround()) {
			BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
			int i = Math.min(16, 2 + level);
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			Iterator var7 = BlockPos.iterate(blockPos.add(-i, -1, -i), blockPos.add(i, -1, i)).iterator();

			while(var7.hasNext()) {
				BlockPos blockPos2 = (BlockPos)var7.next();
				if (blockPos2.isWithinDistance(entity.getPos(), (double)i)) {
					mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
					BlockState blockState2 = world.getBlockState(mutable);
					if (blockState2.isAir()) {
						BlockState blockState3 = world.getBlockState(blockPos2);
						if (blockState3 == FrostedIceBlock.getMeltedState() && blockState.canPlaceAt(world, blockPos2) && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
							world.setBlockState(blockPos2, blockState);
							world.scheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getRandom(), 60, 120));
						}
					}
				}
			}
		}
	}
	@Unique
	private List<BlockPos> replacedWaterBlocks = new ArrayList<>();

	@Unique
	public void restoreReplacedWaterBlocks(World world) {
		for (BlockPos pos : replacedWaterBlocks) {
			world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
		}
		// 清空替换过的水方块列表
		replacedWaterBlocks.clear();
	}

	@Unique
	public void checkAndReplaceWaterBlocks(World world, BlockPos playerPos) {
		int radius = 4; // 3×3范围检索

		for (int yOffset = -3; yOffset <= 30; yOffset++) {
			for (int xOffset = -radius; xOffset <= radius; xOffset++) {
				for (int zOffset = -radius; zOffset <= radius; zOffset++) {
					BlockPos targetPos = playerPos.add(xOffset, yOffset, zOffset);

					if (isWithin3x3(playerPos, targetPos) && world.getBlockState(targetPos).getBlock() == Blocks.WATER) {
						// 如果方块在2x2范围内且是水方块，替换为空气方块
						replacedWaterBlocks.add(targetPos);
						world.setBlockState(targetPos, Blocks.STRUCTURE_VOID.getDefaultState(), 3);
					}

					if (!isWithin3x3(playerPos, targetPos) & replacedWaterBlocks.contains(targetPos)) {
						restoreReplacedWaterBlocks(world);
					}
				}
			}
		}
	}


	@Unique
	private boolean isWithin3x3(BlockPos playerPos, BlockPos targetPos) {
		int deltaX = Math.abs(playerPos.getX() - targetPos.getX());
		int deltaY = Math.abs(playerPos.getY() - targetPos.getY());
		int deltaZ = Math.abs(playerPos.getZ() - targetPos.getZ());

		return deltaX <= 3 && deltaY <= 30 && deltaZ <= 3;
	}
}