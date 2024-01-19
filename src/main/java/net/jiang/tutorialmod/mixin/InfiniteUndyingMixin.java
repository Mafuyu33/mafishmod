package net.jiang.tutorialmod.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

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

    /**
     * @author
     * Mafuyu33
     * @reason
     * Change the totem of undying code
     */
    @Overwrite
    private boolean tryUseTotem(DamageSource source) {
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
//                    itemStack2.decrement(1);
                    break;
                }
            }

            if (itemStack != null) {
                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,900,(int) (Times-1F)));
                this.setHealth(1);
//                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
//                this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
//                this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                this.getWorld().sendEntityStatus(this, (byte)35);
                Times++;
            }

            return itemStack != null;
        }
    }
}
