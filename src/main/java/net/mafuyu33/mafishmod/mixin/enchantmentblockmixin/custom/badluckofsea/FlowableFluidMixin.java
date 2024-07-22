package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.badluckofsea;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin {

	@Inject(at = @At("HEAD"), method = "receivesFlow", cancellable = true)
	private void init1(Direction face, BlockView world, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState, CallbackInfoReturnable<Boolean> cir) {
		int k = BlockEnchantmentStorage.getLevel(ModEnchantments.BAD_LUCK_OF_SEA,pos);
		if(k>0){
			System.out.println("receivesFlow");
			// 获取当前方块的世界对象，必须确保world是World类型
			if (world instanceof World mutableWorld) {
				// 破坏方块
				mafishmod$generateFallingBlock(pos,state,mutableWorld);
			}
			cir.setReturnValue(true);
		}
	}


	@Unique
	private void mafishmod$generateFallingBlock(BlockPos targetPos ,BlockState blockState, World world) {
		if(!world.isClient()) {
			BlockEntity blockEntity = world.getBlockEntity(targetPos);

			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(targetPos), new NbtList())) {
				BlockEnchantmentStorage.removeBlockEnchantment(targetPos.toImmutable());//删除信息
			}

			FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);

			fallingBlockEntity.block = blockState;
			fallingBlockEntity.timeFalling = 1;
			fallingBlockEntity.setNoGravity(false);
			fallingBlockEntity.intersectionChecked = true;
			fallingBlockEntity.setPosition(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
			fallingBlockEntity.setVelocity(0, 0.2, 0);
			fallingBlockEntity.prevX = targetPos.getX() + 0.5;
			fallingBlockEntity.prevY = targetPos.getY();
			fallingBlockEntity.prevZ = targetPos.getZ() + 0.5;
			fallingBlockEntity.setFallingBlockPos(targetPos);
			//设置附魔
			//设置伤害
			fallingBlockEntity.setHurtEntities(0, -1);

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


}