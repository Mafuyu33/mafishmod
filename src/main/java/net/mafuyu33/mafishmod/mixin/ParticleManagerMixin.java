package net.mafuyu33.mafishmod.mixin;

import net.mafuyu33.mafishmod.util.EvictingLinkedHashSetQueue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Queue;
import java.util.function.Function;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {//增加粒子数量上限

//	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), index = 1)
//	private EvictingQueue<Particle> injected(EvictingQueue<Particle> originalQueue) {
//		int maxSize = 10; // 设置新的最大大小值
//		return EvictingQueue.create(maxSize);
//	}

	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), index = 1)
	private Function<ParticleTextureSheet, Queue<Particle>> madparticleUseEvictingLinkedHashSetQueueInsteadOfEvictingQueue(Function<ParticleTextureSheet, Queue<Particle>> mappingFunction) {
		return t -> new EvictingLinkedHashSetQueue<>(16384, 999999999);
	}




}