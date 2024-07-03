package net.mafuyu33.mafishmod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SwitchItem extends Item {
    public SwitchItem(Settings settings) {
        super(settings);
    }
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks % 20==0){
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));         // 中毒
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));       // 虚弱
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));       // 缓慢
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 0)); // 挖掘疲劳
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));      // 失明
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 0));         // 饥饿
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0));         // 反胃
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));         // 凋零
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 0));     // 漂浮
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 100, 0));         // 不幸
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0));
        }
    }
}


