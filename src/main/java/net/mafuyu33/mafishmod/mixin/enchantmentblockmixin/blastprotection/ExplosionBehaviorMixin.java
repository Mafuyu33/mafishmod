package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.blastprotection;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityExplosionBehavior.class)
public abstract class ExplosionBehaviorMixin {
	@Inject(at = @At(value = "HEAD"), method = "canDestroyBlock",cancellable = true)
	private void init(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power, CallbackInfoReturnable<Boolean> cir) {
		int i=BlockEnchantmentStorage.getLevel(Enchantments.BLAST_PROTECTION,pos);
		if(i>0){
			cir.setReturnValue(false);
		}
	}
}