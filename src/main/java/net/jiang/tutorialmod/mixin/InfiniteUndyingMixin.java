package net.jiang.tutorialmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

import static net.minecraft.enchantment.ThornsEnchantment.getDamageAmount;

@Mixin(LivingEntity.class)
public abstract class InfiniteUndyingMixin extends Entity implements Attackable{
    @Unique
    float Times = 0F;
    public InfiniteUndyingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract boolean clearStatusEffects();

    @Shadow
    public final boolean addStatusEffect(StatusEffectInstance effect) {
        return this.addStatusEffect(effect, (Entity)null);
    }

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);


    @Shadow @Final private static TrackedData<Float> HEALTH;

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow @Nullable protected PlayerEntity attackingPlayer;

    @Unique
    private void explode() {
        float f = 4.0F;
        this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), f, World.ExplosionSourceType.TNT);
    }

    /**
     * @author
     * Mafuyu33
     * @reason
     * Change the totem of undying code
     */
    @Overwrite
    private boolean tryUseTotem(DamageSource source) {

        Entity attacker = source.getAttacker();
        Entity user= source.getSource();
        Vec3d position=source.getPosition();

        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            ItemStack itemStack = null;
            Hand[] var4 = Hand.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Hand hand = var4[var6];
                ItemStack itemStack2 = this.getStackInHand(hand);
                if (itemStack2.isOf(Items.TOTEM_OF_UNDYING)) {
                    itemStack = itemStack2.copy();
                    int k = EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStack);
                    if (k == 0) {
                        itemStack2.decrement(1);
                    }

                    break;
                }
            }

            if (itemStack != null) {

                this.setHealth(1.0F);
                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));



                int i = EnchantmentHelper.getLevel(Enchantments.PROTECTION, itemStack);//保护
                if (i > 0) {
                    this.clearStatusEffects();
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,900,(int) (Times-1F)));
                    this.setHealth(1);
                }

                int j = EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, itemStack);//爆炸保护
                if (j > 0) {
                    if (attacker != null && user != null) {
//                      BlockPos blockPos = user.getBlockPos();
                        explode();
                    }
                }

                int k = EnchantmentHelper.getLevel(Enchantments.CHANNELING, itemStack);//引雷
                if (k > 0) {
                    if (this.getWorld() instanceof ServerWorld) {
                        if (attacker != null && user != null) {
                            BlockPos blockPos = attacker.getBlockPos();
                            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
                            if (lightningEntity != null) {
                                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                                lightningEntity.setChanneler(user instanceof ServerPlayerEntity ? (ServerPlayerEntity) user : null);
                                this.getWorld().spawnEntity(lightningEntity);
                                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                                this.playSound(soundEvent, 5, 1.0F);
                            }
                        }
                    }
                }
//                        if (entry != null) {
//                            ((ItemStack)entry.getValue()).damage(2, user, (entity) -> {
//                                entity.sendEquipmentBreakStatus((EquipmentSlot)entry.getKey());
//                            });
//                        }





                this.getWorld().sendEntityStatus(this, (byte)35);
                Times++;
            }

            return itemStack != null;
        }
    }
}
