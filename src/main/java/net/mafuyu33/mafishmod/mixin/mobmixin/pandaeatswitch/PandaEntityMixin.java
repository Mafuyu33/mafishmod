package net.mafuyu33.mafishmod.mixin.mobmixin.pandaeatswitch;

import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PandaEntity.class)
public abstract class PandaEntityMixin extends AnimalEntity {
	protected PandaEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow public abstract boolean isEating();

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo ci) {
		if(this.isEating() && this.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.SWITCH)){
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));         // 中毒
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));       // 虚弱
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));       // 缓慢
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 0)); // 挖掘疲劳
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));      // 失明
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 0));         // 饥饿
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0));         // 反胃
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));         // 凋零
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 0));     // 漂浮
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 100, 0));         // 不幸
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0));
		}
	}
}