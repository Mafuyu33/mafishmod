package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.luoyangshovel;

import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends MiningToolItem {
	public ShovelItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@Inject(at = @At("HEAD"), method = "useOnBlock",cancellable = true)
	private void init(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		int k = EnchantmentHelper.getLevel(Enchantments.POWER, context.getStack());
		if (k > 0) {
			mafishmod$generateFallingBlock(context.getBlockPos(),context.getWorld().getBlockState(context.getBlockPos()), context.getWorld(),k,context.getPlayer());
			cir.setReturnValue(ActionResult.success(context.getWorld().isClient));
		}
	}

	@Unique
	private void mafishmod$generateFallingBlock(BlockPos targetPos ,BlockState blockState, World world ,int power, PlayerEntity user) {
		if(!world.isClient()) {
			BlockEntity blockEntity = world.getBlockEntity(targetPos);

			FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);

			fallingBlockEntity.block = blockState;
			fallingBlockEntity.timeFalling = 1;
			fallingBlockEntity.setNoGravity(false);
			fallingBlockEntity.intersectionChecked = true;
			fallingBlockEntity.setPosition(targetPos.getX() + 0.5, targetPos.getY() + 1.2, targetPos.getZ() + 0.5);
			fallingBlockEntity.setVelocity(Vec3d.ZERO);
			fallingBlockEntity.prevX = targetPos.getX() + 0.5;
			fallingBlockEntity.prevY = targetPos.getY() + 1.2;
			fallingBlockEntity.prevZ = targetPos.getZ() + 0.5;
			fallingBlockEntity.setFallingBlockPos(targetPos);
			//设置速度
			mafishmod$launchBlock(targetPos, power, user, fallingBlockEntity);
			//设置伤害
			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(targetPos), new NbtList())) {//附魔海之嫌弃
//				TutorialMod.LOGGER.info(String.valueOf("附魔海之嫌弃"));
				fallingBlockEntity.setHurtEntities(0, -1);
				BlockEnchantmentStorage.removeBlockEnchantment(targetPos.toImmutable());//删除信息
			} else {
//				TutorialMod.LOGGER.info(String.valueOf("没附魔海之嫌弃"));
				fallingBlockEntity.setHurtEntities(50, power * 2);
			}
			// 如果方块有附加的 BlockEntity 数据，可以设置 blockEntityData 字段
			if (blockEntity != null) {
				NbtCompound blockEntityData = new NbtCompound();
				blockEntity.writeNbt(blockEntityData);
				fallingBlockEntity.blockEntityData = blockEntityData;
			}

			world.setBlockState(targetPos, Blocks.AIR.getDefaultState(), 3);

			world.spawnEntity(fallingBlockEntity);
		}

	}

	@Unique
	private static void mafishmod$launchBlock( BlockPos targetPos, int power, PlayerEntity user, FallingBlockEntity fallingBlockEntity) {
		// 获取用户的位置
		Vec3d userPos = user.getPos();

		// 获取目标位置并转换为方块中心的浮点坐标
		Vec3d targetVec = new Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);

		// 计算指向目标位置的向量
		Vec3d direction = targetVec.subtract(userPos).normalize();

		// 将Y轴的分量稍微增加一点
		direction = new Vec3d(direction.x, direction.y + 0.2, direction.z).normalize();

		// 根据power调整速度
		Vec3d velocity = direction.multiply(power*0.5);

		// 设置方块实体的速度
		fallingBlockEntity.setVelocity(velocity);
	}
}