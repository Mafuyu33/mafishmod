package net.jiang.tutorialmod.mixin;

import dev.architectury.hooks.level.biome.EffectsProperties;
import dev.architectury.platform.Mod;
import net.jiang.tutorialmod.effect.ModStatusEffects;
import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.jiang.tutorialmod.mixinhelper.FireworkRocketEntityMixinHelper;
import net.jiang.tutorialmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class WeaponEnchantmentMixin extends Entity implements Attackable{
    @Unique
    float Times = 0F;

    @Shadow public abstract Hand getActiveHand();

    @Shadow public abstract ItemStack getStackInHand(Hand hand);

    @Shadow public abstract boolean isAlive();

    @Shadow @Final protected static TrackedData<Byte> LIVING_FLAGS;

    @Shadow public abstract void setStackInHand(Hand hand, ItemStack stack);

    @Shadow public abstract boolean isDead();


    public WeaponEnchantmentMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("RETURN"), method = "onStatusEffectApplied")
    private void init1(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        if(source != null && effect.getEffectType() == ModStatusEffects.TELEPORT_EFFECT && this != source){
            // 获取攻击者（当前实体）和目标实体的位置
            Vec3d attackerPos = source.getPos();
            Vec3d targetPos = this.getPos();

            // 交换两个实体的位置
            source.teleport(targetPos.x, targetPos.y, targetPos.z);
            this.teleport(attackerPos.x, attackerPos.y, attackerPos.z);
        }
    }
        @Inject(at = @At("RETURN"), method = "onAttacking")
    private void init(Entity target, CallbackInfo info) {

        Hand hand = this.getActiveHand();
        ItemStack itemStack = this.getStackInHand(hand);
        int k = EnchantmentHelper.getLevel(ModEnchantments.KILL_CHICKEN_GET_EGG, itemStack);
        int j = EnchantmentHelper.getLevel(Enchantments.LOOTING, itemStack);
        int m = EnchantmentHelper.getLevel(ModEnchantments.GONG_XI_FA_CAI,itemStack);
        int n = EnchantmentHelper.getLevel(ModEnchantments.MERCY,itemStack);
        int o = EnchantmentHelper.getLevel(ModEnchantments.HOT_POTATO,itemStack);
        int p = EnchantmentHelper.getLevel(ModEnchantments.REVERSE,itemStack);

        if(p>0 && target instanceof LivingEntity livingEntity && livingEntity.isAlive() && !getWorld().isClient){//反转了
            if(!target.hasCustomName()) {
                target.setCustomName(Text.of("Grumm"));
                target.setCustomNameVisible(false);
                WeaponEnchantmentMixinHelper.storeReverse(target.getUuid(),1);
            }else {
                target.setCustomName(null);
                WeaponEnchantmentMixinHelper.storeReverse(target.getUuid(),0);
            }
        }

        if (o>0 && target instanceof LivingEntity livingEntity && livingEntity.isAlive()){//烫手山芋
//            ItemStack targetItemStack = ((LivingEntity) target).getStackInHand(targetHand);
//            itemStack.damage(1,random, Objects.requireNonNull(getServer()).getCommandSource().getPlayer());
            Hand targetHand = ((LivingEntity) target).getActiveHand();
            if (!livingEntity.getMainHandStack().isEmpty()) {
                dropStack(livingEntity.getMainHandStack());
            }
            livingEntity.setStackInHand(targetHand, itemStack.copy());
            this.setStackInHand(hand, ItemStack.EMPTY);
        }
        
        if (k > 0 && target instanceof ChickenEntity) {//杀鸡取卵
            target.dropItem(Items.EGG);
        }
        if(k > 0 && j>0 && target instanceof ChickenEntity){
            target.dropItem(Items.EGG);
            target.dropItem(Items.DRAGON_EGG);
            target.dropItem(Items.TURTLE_EGG);
            target.dropItem(Items.SNIFFER_EGG);
            target.dropItem(Items.FROGSPAWN);
        }


        if (m > 0 && target.getType() == EntityType.VILLAGER) {//恭喜发财
            WeaponEnchantmentMixinHelper.storeEntityValue(target.getId(),m) ;
        }


        if(n > 0 && target instanceof LivingEntity){
            Times=Times+n;
            ((LivingEntity) target).addStatusEffect(
                    new StatusEffectInstance(StatusEffects.HEALTH_BOOST,900,(int) (Times-1F)));
            ((LivingEntity) target).addStatusEffect(
                    new StatusEffectInstance(StatusEffects.INSTANT_HEALTH,900,(int) (Times-1F)));
        }
    }
    @Unique
    boolean isDrop=false;
    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo ci) {
        Hand hand = this.getActiveHand();
        ItemStack itemStack = this.getStackInHand(hand);
        int o = EnchantmentHelper.getLevel(ModEnchantments.HOT_POTATO,itemStack);
        if(o>0 && this.isDead() && !isDrop){
            dropStack(itemStack);
            isDrop=true;
        }
    }
}