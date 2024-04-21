package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class BadLuckOfSeaItemEntityMixin extends Entity implements Ownable {

	@Shadow
	public abstract ItemStack getStack();

	public BadLuckOfSeaItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {//实现丢出去的实体被水排斥
		ItemStack itemStack = this.getStack();
		Item item = itemStack.getItem();
		World world = this.getWorld();
		BlockPos blockPos = this.getBlockPos();
		FluidState fluidState = world.getFluidState(blockPos);
		if (fluidState.isIn(FluidTags.WATER) & EnchantmentHelper.getLevel(ModEnchantments.BAD_LUCK_OF_SEA, itemStack) > 0) {

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
				//this.sendMessage(Text.literal("检测到最近的固体方块可弹射"));

				// 计算方向向量
				Vec3d direction = Vec3d.ofCenter(closestNonLiquidBlockPos).subtract(this.getPos()).normalize();

				double speed = 0.5; // 设定速度大小（可以根据需要调整）

				// 计算最终的速度向量
				Vec3d velocity = direction.multiply(speed);

				this.addVelocity(0, 1, 0);
				this.addVelocity(velocity); // 应用速度
			}
		}
	}
}