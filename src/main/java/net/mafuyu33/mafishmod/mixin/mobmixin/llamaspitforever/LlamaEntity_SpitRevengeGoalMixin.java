package net.mafuyu33.mafishmod.mixin.mobmixin.llamaspitforever;

import net.mafuyu33.mafishmod.config.ConfigHelper;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.LlamaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LlamaEntity.SpitRevengeGoal.class)
public abstract class LlamaEntity_SpitRevengeGoalMixin extends RevengeGoal {
	public LlamaEntity_SpitRevengeGoalMixin(PathAwareEntity mob, Class<?>... noRevengeTypes) {
		super(mob, noRevengeTypes);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/LlamaEntity;setSpit(Z)V"), method = "shouldContinue",cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> cir) {
		boolean isLlamaSpitForever =ConfigHelper.isLlamaSpitForever();
		if(isLlamaSpitForever) {
			if(target!=null && target.isAlive()) {
				cir.setReturnValue(true);
			}
		}
	}
}