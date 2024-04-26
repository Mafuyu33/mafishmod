package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.respiration;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FlowableFluid;beforeBreakingBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"), method = "flow",cancellable = true)
	private void init(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci) {
		if(BlockEnchantmentStorage.getLevel(Enchantments.RESPIRATION,pos)>0){
			ci.cancel();
		}
	}
}