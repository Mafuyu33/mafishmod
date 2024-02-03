package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.jiang.tutorialmod.event.KeyInputHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.KeybindTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class ThrowItemMixin extends LivingEntity  {
	protected ThrowItemMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {//实现丢出去的实体被乘上一个速度
//		World world = getWorld();
//
//		Vec3d direction = this.getRotationVector(); // 获取玩家面朝方向
//		// 获取丢出的物品实体
//		Vec3d playerPos = this.getPos();
//		Entity itemEntity = null;
//
//		// 定义一个用于检测的区域，以玩家为中心
//		Box detectionBox = new Box(this.getBlockPos().add(-5, -5, -5), this.getBlockPos().add(5, 5, 5));
//
//		// 获取玩家周围的实体（排除玩家自己）
//		List<Entity> nearbyEntities = world.getOtherEntities(this, detectionBox);
//
//		for (Entity entity : nearbyEntities) {
//			if (entity instanceof ItemEntity) {
//				ItemEntity possibleItemEntity = (ItemEntity) entity;
//				Vec3d entityPos = possibleItemEntity.getPos();
//				itemEntity = possibleItemEntity;
//                    // 检查是否是刚刚丢出的物品实体
//                    if (entityPos.distanceTo(playerPos) < 1.0) {
//                        itemEntity = possibleItemEntity;
//                        break;
//                    }
//			}
//		}
//
//		// 在这里可以操作刚刚丢出的物品实体 (itemEntity)
//		if (itemEntity != null) {
//
////                itemEntity.move(MovementType.SELF,direction.multiply(power));
//			itemEntity.addVelocity(direction.multiply(power)); // 根据力度设置速度向量
//			this.sendMessage(Text.literal((String.valueOf(itemEntity))));
//		} else {
//			this.sendMessage(Text.literal((String.valueOf(123))));
//			// 物品实体未生成，处理错误或等待更长时间
//		}

	}
}