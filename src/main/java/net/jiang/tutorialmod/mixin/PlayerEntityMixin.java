package net.jiang.tutorialmod.mixin;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
//    @Inject(at = @At("HEAD"), method = "tick")
//    private void init(CallbackInfo ci) {
//    }
}
