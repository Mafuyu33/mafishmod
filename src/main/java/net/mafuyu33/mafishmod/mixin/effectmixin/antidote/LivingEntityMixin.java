package net.mafuyu33.mafishmod.mixin.effectmixin.antidote;

import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

import static net.minecraft.entity.effect.StatusEffectCategory.HARMFUL;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	@Shadow protected abstract void onStatusEffectRemoved(StatusEffectInstance effect);

	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);
	@Final
	@Shadow private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;


	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		if(this.hasStatusEffect(ModStatusEffects.ANTIDOTE_EFFECT)){
			if (!this.getWorld().isClient) { // 确保该代码仅在服务器端运行
				Iterator<StatusEffectInstance> iterator = this.activeStatusEffects.values().iterator(); // 获取活跃状态效果的迭代器
				boolean bl; // 声明一个布尔变量
				for(bl = false; iterator.hasNext(); bl = true) { // 初始化bl为false，然后遍历活跃状态效果列表
					StatusEffectInstance effect = iterator.next(); // 获取当前状态效果
					if (mafishmod$isNegativeEffect(effect)) { // 判断是否为负面效果
						this.onStatusEffectRemoved(effect); // 调用onStatusEffectRemoved方法处理当前状态效果
						iterator.remove(); // 从活跃状态效果列表中移除当前状态效果
					}
				}
			}
		}
	}

	@Unique
	private boolean mafishmod$isNegativeEffect(StatusEffectInstance effect) {
		// 你需要根据实际情况实现此方法，判断效果类型是否为负面效果
		// 例如，根据效果类型进行判断
		return effect.getEffectType().getCategory()==HARMFUL;
	}
}