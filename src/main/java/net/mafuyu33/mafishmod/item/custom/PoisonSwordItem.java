package net.mafuyu33.mafishmod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class PoisonSwordItem extends SwordItem {
    public PoisonSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    private int delayTimer = 0;

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (delayTimer < 16) {
            delayTimer++;
        } else {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 400, 0));
            delayTimer = 0;
        }


        if(user.getHealth()==1){
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE,10,0));
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,200,0));
        return super.postHit(stack, target, attacker);
    }
}
