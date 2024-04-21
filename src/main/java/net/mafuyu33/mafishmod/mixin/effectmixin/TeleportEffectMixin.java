package net.mafuyu33.mafishmod.mixin.effectmixin;

import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.mafuyu33.mafishmod.item.custom.ColliableItem;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class TeleportEffectMixin extends Entity implements Attackable {

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean isAlive();
    @Unique
    int time = 0;

    public TeleportEffectMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isCollidable() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        if(this.isPlayer() && this.hasStatusEffect(ModStatusEffects.TELEPORT_EFFECT)) {//传送药水
            time++;
            if (time > 1) {
                randomTeleport(this.getWorld(), (LivingEntity) (Object) this);
                time=0;
            }
        }
    }

    @Unique
    private void randomTeleport(World world, LivingEntity user) {
        if (!world.isClient) {
            for(int i = 0; i < 16; ++i) {
                double d = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                double e = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
                double f = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                if (user.hasVehicle()) {
                    user.stopRiding();
                }

                Vec3d vec3d = user.getPos();
                if (user.teleport(d, e, f, true)) {
                    world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
                    SoundCategory soundCategory;
                    SoundEvent soundEvent;
                    if (user instanceof FoxEntity) {
                        soundEvent = SoundEvents.ENTITY_FOX_TELEPORT;
                        soundCategory = SoundCategory.NEUTRAL;
                    } else {
                        soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                        soundCategory = SoundCategory.PLAYERS;
                    }

                    world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), soundEvent, soundCategory);
                    user.onLanding();
                    break;
                }
            }
        }
    }
}
