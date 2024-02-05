package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class ArmorEnchantmentMixin extends Entity implements Attackable {
	@Shadow public abstract Iterable<ItemStack> getArmorItems();

	protected ArmorEnchantmentMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
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

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		Iterable<ItemStack> armorItems = this.getArmorItems();

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
			}
//			if (armorItem.getItem() instanceof ArmorItem) {//所有装备
//				int k = EnchantmentHelper.getLevel(Enchantments., armorItem);//多重射击
//				if (k > 0) {
//					World world = this.getWorld();
//					BlockPos blockPos = this.getBlockPos();
//					if (this.) {
//						this.addVelocity(0, 1, 0);
//					}
//				}
//			}

		}
	}
}