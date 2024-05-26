package net.mafuyu33.mafishmod.mixin.mobmixin.llamaspitforever;

import net.mafuyu33.mafishmod.config.ConfigHelper;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.LlamaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LlamaEntity.class)
public abstract class LlamaEntityMixin{
	@Inject(at = @At(value = "HEAD"), method = "initGoals")
	private void init(CallbackInfo ci) {
		boolean isLlamaSpitForever =ConfigHelper.isLlamaSpitForever();
        LlamaEntity self = (LlamaEntity) (Object) this;
        if(isLlamaSpitForever) {
            self.goalSelector.getGoals().removeIf(goal -> goal.getGoal() instanceof ProjectileAttackGoal);
			self.goalSelector.add(3, new ProjectileAttackGoal(self, 1.25, 1, 20.0F));
		}else {
            self.goalSelector.getGoals().removeIf(goal -> goal.getGoal() instanceof ProjectileAttackGoal);
			self.goalSelector.add(3, new ProjectileAttackGoal(self, 1.25, 40, 20.0F));
		}
	}
}