package net.mafuyu33.mafishmod.mixin.mobmixin.goatdashforever;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.mafuyu33.mafishmod.config.ConfigHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.PrepareRamTask;
import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.passive.GoatBrain;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoatBrain.class)
public abstract class GoatBrainMixin {

	@Mutable
	@Final
	@Shadow
	private static TargetPredicate RAM_TARGET_PREDICATE;
//	@Mutable
//	@Shadow @Final
//	public static int MIN_RAM_TARGET_DISTANCE;
//	@Mutable
//	@Shadow @Final
//	public static int MAX_RAM_TARGET_DISTANCE;
	@Mutable
	@Shadow @Final
	private static UniformIntProvider RAM_COOLDOWN_RANGE;
	@Mutable
	@Shadow @Final
	private static UniformIntProvider SCREAMING_RAM_COOLDOWN_RANGE;


	@Inject(at = @At("HEAD"), method = "updateActivities")
	private static void init(GoatEntity goat, CallbackInfo ci) {
		boolean isGoatDashForever = ConfigHelper.isGoatDashForever();
		boolean isGoatDashTogether = ConfigHelper.isGoatDashTogether();
		if(isGoatDashTogether){
			RAM_TARGET_PREDICATE = TargetPredicate.createAttackable().setPredicate((entity) -> entity.getWorld().getWorldBorder().contains(entity.getBoundingBox()));
		}else {
			RAM_TARGET_PREDICATE = TargetPredicate.createAttackable().setPredicate((entity) -> !entity.getType().equals(EntityType.GOAT) && entity.getWorld().getWorldBorder().contains(entity.getBoundingBox()));
		}
		if(isGoatDashForever){
//			MIN_RAM_TARGET_DISTANCE=1;
//			MAX_RAM_TARGET_DISTANCE=20;
			RAM_COOLDOWN_RANGE = UniformIntProvider.create(0, 1);
			SCREAMING_RAM_COOLDOWN_RANGE = UniformIntProvider.create(0, 1);
		}else {
//			MIN_RAM_TARGET_DISTANCE=4;
//			MAX_RAM_TARGET_DISTANCE=7;
			RAM_COOLDOWN_RANGE = UniformIntProvider.create(600, 6000);
			SCREAMING_RAM_COOLDOWN_RANGE = UniformIntProvider.create(100, 300);
		}

	}
	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * For dash Forever
	 */
	@Overwrite
	private static void addRamActivities(Brain<GoatEntity> brain) {
		boolean isGoatDashForever = ConfigHelper.isGoatDashForever();
		if (isGoatDashForever) {
			brain.setTaskList(Activity.RAM, ImmutableList.of(Pair.of(0, new RamImpactTask((goat) -> {
				return goat.isScreaming() ? SCREAMING_RAM_COOLDOWN_RANGE : RAM_COOLDOWN_RANGE;
			}, RAM_TARGET_PREDICATE, 3.0F, (goat) -> {
				return goat.isBaby() ? 1.0 : 2.5;
			}, (goat) -> {
				return goat.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_RAM_IMPACT : SoundEvents.ENTITY_GOAT_RAM_IMPACT;
			}, (goat) -> {
				return goat.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_HORN_BREAK : SoundEvents.ENTITY_GOAT_HORN_BREAK;
			})), Pair.of(1, new PrepareRamTask<>((goat) -> {
				return goat.isScreaming() ? SCREAMING_RAM_COOLDOWN_RANGE.getMin() : RAM_COOLDOWN_RANGE.getMin();
			}, 1, 20, 1.25F, RAM_TARGET_PREDICATE, 0, (goat) -> {
				return goat.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_PREPARE_RAM : SoundEvents.ENTITY_GOAT_PREPARE_RAM;
			}))), ImmutableSet.of(Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.BREED_TARGET, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT)));
		}else {
			brain.setTaskList(Activity.RAM, ImmutableList.of(Pair.of(0, new RamImpactTask((goat) -> {
				return goat.isScreaming() ? SCREAMING_RAM_COOLDOWN_RANGE : RAM_COOLDOWN_RANGE;
			}, RAM_TARGET_PREDICATE, 3.0F, (goat) -> {
				return goat.isBaby() ? 1.0 : 2.5;
			}, (goat) -> {
				return goat.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_RAM_IMPACT : SoundEvents.ENTITY_GOAT_RAM_IMPACT;
			}, (goat) -> {
				return goat.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_HORN_BREAK : SoundEvents.ENTITY_GOAT_HORN_BREAK;
			})), Pair.of(1, new PrepareRamTask<>((goat) -> {
				return goat.isScreaming() ? SCREAMING_RAM_COOLDOWN_RANGE.getMin() : RAM_COOLDOWN_RANGE.getMin();
			}, 4, 7, 1.25F, RAM_TARGET_PREDICATE, 20, (goat) -> {
				return goat.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_PREPARE_RAM : SoundEvents.ENTITY_GOAT_PREPARE_RAM;
			}))), ImmutableSet.of(Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.BREED_TARGET, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT)));
		}
	}
}