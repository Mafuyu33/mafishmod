package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.main;

import com.llamalad7.mixinextras.sugar.Local;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.mixinhelper.InjectHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/math/BlockPos;"
			,ordinal = 1), method = "move")//活塞推拉方块的部分
	private void init(World world, BlockPos pos, Direction dir
			, boolean retract, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 2) BlockPos blockPos3) {
		if (!world.isClient) {//只在服务端运行,获取信息
			System.out.println(blockPos3);

			Direction direction = retract ? dir : dir.getOpposite();

			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos3), new NbtList())) {//如果原位置方块有附魔
				NbtList enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos3); //获取附魔信息列表
				BlockEnchantmentStorage.addBlockEnchantment(blockPos3.offset(direction).toImmutable(), enchantments);//在新位置储存信息
			}
			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos3), new NbtList())) {
				BlockEnchantmentStorage.removeBlockEnchantment(blockPos3.toImmutable());//删除信息
			}
		}
	}
//	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
//			,ordinal = 4), method = "move",locals = LocalCapture.CAPTURE_FAILSOFT)//替换成空气的部分
//	private void init1(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos4) {//活塞推拉方块
//		if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos4), new NbtList())) {
//			BlockEnchantmentStorage.removeBlockEnchantment(blockPos4.toImmutable());//删除信息
//		}
//	}
}