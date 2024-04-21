package net.mafuyu33.mafishmod.mixin.itemmixin.SwimmingTripwire;


import net.mafuyu33.mafishmod.mixinhelper.TripwireBlockMixinHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);

    }
    @Inject(at = @At("RETURN"), method = "updatePose")
    private void init(CallbackInfo ci) {
        if(TripwireBlockMixinHelper.getEntityValue(this.getId())>0){
            EntityPose entityPose3 = EntityPose.SWIMMING;
            this.setPose(entityPose3);
            TripwireBlockMixinHelper.storeEntityValue(this.getId(),TripwireBlockMixinHelper.getEntityValue(this.getId())-1);
        }
    }
}
