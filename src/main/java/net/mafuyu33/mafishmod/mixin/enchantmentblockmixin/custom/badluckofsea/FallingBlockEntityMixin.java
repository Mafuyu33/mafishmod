package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.badluckofsea;

import com.llamalad7.mixinextras.sugar.Local;
import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
	@Shadow private int fallHurtMax;
	public FallingBlockEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		World world = this.getWorld();
		BlockPos blockPos = this.getBlockPos();
		FluidState fluidState = world.getFluidState(blockPos);
		if (fluidState.isIn(FluidTags.WATER) && this.fallHurtMax==-1) {

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

				double speed = 0.15; // 设定速度大小（可以根据需要调整）

				// 计算最终的速度向量
				Vec3d velocity = direction.multiply(speed);

				this.addVelocity(0, 0.3, 0);
				this.addVelocity(velocity); // 应用速度
			}else {
				this.addVelocity(0, 0.3, 0);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), method = "tick")
	private void init1(CallbackInfo info, @Local BlockPos blockPos) {
		if(this.fallHurtMax==-1) {
			Enchantment enchantment = ModEnchantments.BAD_LUCK_OF_SEA;
			NbtList enchantmentNbtList = new NbtList();
			// 将 Enchantment 对象转换为 NBT 形式
			NbtCompound enchantmentNbt = new NbtCompound();
			enchantmentNbt.putString("id", String.valueOf(Registries.ENCHANTMENT.getId(enchantment)));
			enchantmentNbt.putInt("lvl", 3);

			// 将 NBT 数据添加到 NbtList
			enchantmentNbtList.add(enchantmentNbt);
			BlockEnchantmentStorage.addBlockEnchantment(blockPos.toImmutable(), enchantmentNbtList);//储存信息
		}
	}
}