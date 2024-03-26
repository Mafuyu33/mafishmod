package net.mafuyu33.mafishmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class InfiniteFoodMixin {
    @Mixin(LivingEntity.class)
    public abstract static class InFiniteFoodMixin extends Entity implements Attackable {

        public InFiniteFoodMixin(EntityType<?> type, World world) {
            super(type, world);
        }

//        @Shadow abstract public SoundEvent getEatSound(ItemStack stack);
//
//        @Unique
//        protected abstract void applyFoodEffects(ItemStack stack, World world, InFiniteFoodMixin targetEntity);
//        /**
//         * @author
//         * Mafuyu33
//         * @reason
//         * Let Food eatable forever
//         */
//        @Overwrite
//
//        public ItemStack eatFood(World world, ItemStack stack) {
//            if (stack.isFood()) {
//                world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), this.getEatSound(stack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
//                this.applyFoodEffects(stack, world, (this));
//                if (!(this instanceof PlayerEntity) || !(this).getAbilities().creativeMode) {
//                    stack.decrement(1);
//                }
//
//                this.emitGameEvent(GameEvent.EAT);
//            }
//
//            return stack;
//        }


        @Inject(method = "eatFood", at = @At("HEAD"))
        private void afterEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(stack.isFood()) {
                int k = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack);
                if (k > 0) {
                    stack.increment(1);
                }
            }
        }
    }
}
