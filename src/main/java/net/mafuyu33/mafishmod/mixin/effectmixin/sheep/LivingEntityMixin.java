package net.mafuyu33.mafishmod.mixin.effectmixin.sheep;

import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow public abstract void remove(RemovalReason reason);

	@Shadow public abstract ItemStack eatFood(World world, ItemStack stack);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		if(!this.getWorld().isClient) {
			if (this.hasStatusEffect(ModStatusEffects.SHEEP_EFFECT)) {//变羊药水
				if (!this.isPlayer()) {//如果不是玩家的话
					Vec3d pos = this.getPos();
					EntityType.SHEEP.spawn(((ServerWorld) this.getWorld()), BlockPos.ofFloored(pos), SpawnReason.TRIGGERED);
					this.remove(RemovalReason.KILLED);
				}
			}
		}
	}
}