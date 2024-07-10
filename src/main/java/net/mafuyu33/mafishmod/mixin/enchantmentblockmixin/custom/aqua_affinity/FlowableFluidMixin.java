package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.aqua_affinity;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin {
	@Inject(at = @At("HEAD"), method = "canFlow", cancellable = true)
	private void init(BlockView world, BlockPos fluidPos, BlockState fluidBlockState, Direction flowDirection, BlockPos flowTo, BlockState flowToBlockState, FluidState fluidState, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
		if(BlockEnchantmentStorage.getLevel(Enchantments.AQUA_AFFINITY,flowTo)>0){
			System.out.println("canFlow");
			cir.setReturnValue(true);
		}
	}
	@Inject(at = @At("HEAD"), method = "receivesFlow", cancellable = true)
	private void init1(Direction face, BlockView world, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState, CallbackInfoReturnable<Boolean> cir) {
		if(BlockEnchantmentStorage.getLevel(Enchantments.AQUA_AFFINITY,pos)>0){
			System.out.println("receivesFlow");
			// 获取当前方块的世界对象，必须确保world是World类型
			if (world instanceof World mutableWorld) {
				// 破坏方块
				mutableWorld.breakBlock(pos, true);
			}
			cir.setReturnValue(true);
		}
	}
//	@Inject(at = @At("HEAD"), method = "canFlowThrough", cancellable = true)
//	private void init2(BlockView world, Fluid fluid, BlockPos pos, BlockState state, Direction face, BlockPos fromPos, BlockState fromState, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
//		if(BlockEnchantmentStorage.getLevel(Enchantments.AQUA_AFFINITY,pos)>0){
//			System.out.println("canFlowThrough");
//			cir.setReturnValue(true);
//		}
//	}
//	@Inject(at = @At("HEAD"), method = "canFill", cancellable = true)
//	private void init3(BlockView world, BlockPos pos, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
//		if(BlockEnchantmentStorage.getLevel(Enchantments.AQUA_AFFINITY,pos)>0){
//			System.out.println("canFill");
//			cir.setReturnValue(true);
//		}
//	}
}