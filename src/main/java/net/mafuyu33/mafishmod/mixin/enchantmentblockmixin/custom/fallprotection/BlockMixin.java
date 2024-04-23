package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.fallprotection;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(at = @At("HEAD"), method = "onLandedUpon",cancellable = true)
	private void init(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
		int i = BlockEnchantmentStorage.getLevel(Enchantments.FEATHER_FALLING, pos);
		if (i > 0){
			entity.handleFallDamage(fallDistance, (-0.25F * i + 1), entity.getDamageSources().fall());
			ci.cancel();
		}
	}
}