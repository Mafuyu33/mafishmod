package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.blastprotection;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityExplosionBehavior.class)
public abstract class ExplosionBehaviorMixin {
	@Inject(at = @At(value = "HEAD"), method = "canDestroyBlock",cancellable = true)
	private void init(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power, CallbackInfoReturnable<Boolean> cir) {
		int i=BlockEnchantmentStorage.getLevel(Enchantments.BLAST_PROTECTION,pos);
		if(i>0){
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getBlastResistance",cancellable = true)
	private void init(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Optional<Float>> cir) {
		int i=BlockEnchantmentStorage.getLevel(ModEnchantments.NO_BLAST_PROTECTION,pos);
		if(i>0){
			// 如果需要取消此方块的爆炸抗性计算，返回一个空的 Optional 对象
			cir.setReturnValue(Optional.empty());
			cir.cancel(); // 取消原始方法的执行
		}
	}
}