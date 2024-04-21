package net.mafuyu33.mafishmod.mixin.mobmixin.canclimb;

import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "isClimbing", cancellable = true)
    private void init(CallbackInfoReturnable<Boolean> cir) {
        if(this.hasStatusEffect(ModStatusEffects.SPIDER_EFFECT)) {
            if (this.horizontalCollision) {
                cir.setReturnValue(true);
            }
        }
    }
}

