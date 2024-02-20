package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.event.ChatMessageHandler;
import net.jiang.tutorialmod.item.custom.MathSwordItem;
import net.jiang.tutorialmod.mixinhelper.MathQuestionMixinHelper;
import net.jiang.tutorialmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean isDead();

    @Shadow public abstract Hand getActiveHand();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        super.tick();
        if(this.getType()==EntityType.VILLAGER) {//村民恭喜发财
            int entityId = this.getId();// 获取实体的ID
            int times = WeaponEnchantmentMixinHelper.getEntityValue(entityId);
            if (times > 0) {
                WeaponEnchantmentMixinHelper.storeEntityValue(entityId, times - 1);
                this.dropItem(Items.EMERALD);
            }
        }



        if(MathSwordItem.isMathMode()) {
            PlayerEntity closestPlayer = getEntityWorld().getClosestPlayer(this, 100);
            if (closestPlayer != null && !getWorld().isClient && !this.isDead()) {//出题
                if (!this.isPlayer() && isPlayerStaring(closestPlayer) && !this.hasCustomName()) {//算数
                    String[] questionAndAnswer = generateArithmeticQuestionAndAnswer();

                    String question = questionAndAnswer[0];
                    int answer = Integer.parseInt(questionAndAnswer[1]);
                    MathQuestionMixinHelper.storeEntityValue(this.getId(), answer);

                    this.setCustomName(Text.of(question));
                    this.setCustomNameVisible(true);
                }
            }
            if (this.hasCustomName() && closestPlayer != null) {//检测有没有答对
//            System.out.println("answer = "+MathQuestionMixinHelper.getEntityValue(this.getId()));
//            System.out.println("youranswer = "+ChatMessageHandler.getNumber());

                if (MathQuestionMixinHelper.getEntityValue(this.getId()) == ChatMessageHandler.getNumber()) {
                    closestPlayer.swingHand(getActiveHand());
                    this.damage(getDamageSources().playerAttack(closestPlayer), 10f);
                    this.setCustomName(null);
                }
            }
        }
    }

    @Unique
    private static String[] generateArithmeticQuestionAndAnswer() {
        Random random = new Random();
        int num1 = random.nextInt(20) + 1; // 生成1到10之间的随机数
        int num2 = random.nextInt(20) + 1; // 生成1到10之间的随机数
        int operator = random.nextInt(4); // 生成0到3之间的随机数，用于选择运算符

        String question;
        int answer = 0;

        switch (operator) {
            case 0:
                question = num1 + " + " + num2 + " = ";
                answer = num1 + num2;
                break;
            case 1:
                question = num1 + " - " + num2 + " = ";
                answer = num1 - num2;
                break;
            case 2:
                question = num1 + " * " + num2 + " = ";
                answer = num1 * num2;
                break;
            case 3:
                // 避免除法时出现除不尽的情况，确保答案是整数
                num2 = random.nextInt(num1) + 1; // 重新生成一个小于num1的随机数，避免除数过大
                question = num1 * num2 + " / " + num2 + " = ";
                answer = num1;
                break;
            default:
                question = ""; // 不会发生
        }

        return new String[]{question, String.valueOf(answer)};
    }
    @Unique
    boolean isPlayerStaring(PlayerEntity player) {
            Vec3d vec3d = player.getRotationVec(1.0F).normalize();
            Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double d = vec3d2.length();
            vec3d2 = vec3d2.normalize();
            double e = vec3d.dotProduct(vec3d2);
            return e > 1.0 - 0.025 / d ? player.canSee(this) : false;
    }


}
