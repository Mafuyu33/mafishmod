package net.mafuyu33.mafishmod.mixin.itemmixin;


import net.mafuyu33.mafishmod.event.ChatMessageHandler;
import net.mafuyu33.mafishmod.item.custom.ColliableItem;
import net.mafuyu33.mafishmod.item.custom.MathSwordItem;
import net.mafuyu33.mafishmod.mixinhelper.MathQuestionMixinHelper;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class MathModeMixin extends Entity implements Attackable {

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean isDead();

    @Shadow public abstract Hand getActiveHand();

    @Shadow public abstract boolean isAlive();

    public MathModeMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isCollidable() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {

        if(MathSwordItem.isMathMode()) {//数学领域
            PlayerEntity closestPlayer = getEntityWorld().getClosestPlayer(this, 100);
            int level = MathSwordItem.getLevel();
            if (closestPlayer != null && !getWorld().isClient && !this.isDead()) {//出题
                if (!this.isPlayer() && isPlayerStaring(closestPlayer) && !this.hasCustomName()) {//算数
                    String[] questionAndAnswer = generateArithmeticQuestionAndAnswer(level);

                    String question = questionAndAnswer[0];
                    String questionColored ="§c§l"+question;
                    int answer = Integer.parseInt(questionAndAnswer[1]);
                    MathQuestionMixinHelper.storeEntityValue(this.getId(), answer);

                    this.setCustomName(Text.of(questionColored));
                    this.setCustomNameVisible(true);
                }
            }
            if (this.hasCustomName() && closestPlayer != null) {//检测有没有答对
//            System.out.println("answer = "+MathQuestionMixinHelper.getEntityValue(this.getId()));
//            System.out.println("youranswer = "+ChatMessageHandler.getNumber());

                if (MathQuestionMixinHelper.getEntityValue(this.getId()) == ChatMessageHandler.getNumber()) {
                    closestPlayer.swingHand(getActiveHand());
                    getWorld().playSound(closestPlayer,closestPlayer.getBlockPos(),
                            SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS,1f,1f);
                    this.damage(getDamageSources().playerAttack(closestPlayer), 10f+level*2);
                    this.setCustomName(null);
                    this.setCustomNameVisible(false);
                }
            }
        }else if(MathQuestionMixinHelper.getEntityValue(this.getId()) != 0) {
            this.setCustomName(null);
            this.setCustomNameVisible(false);
        }
    }
    @Unique
    private static String[] generateArithmeticQuestionAndAnswer(int level) {
        Random random = new Random();
        String question;
        int answer = 0;

        do {
            int num1 = random.nextInt(10 * (level + 1)) + 1; // 生成1到10之间的随机数
            int num2 = random.nextInt(10 * (level + 1)) + 1; // 生成1到10之间的随机数
            int operator = random.nextInt(4); // 生成0到3之间的随机数，用于选择运算符

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
                    question = num1 + " × " + num2 + " = ";
                    answer = num1 * num2;
                    break;
                case 3:
                    // 避免除法时出现除不尽的情况，确保答案是整数
                    num2 = random.nextInt(num1) + 1; // 重新生成一个小于num1的随机数，避免除数过大
                    question = num1 * num2 + " ÷ " + num2 + " = ";
                    answer = num1;
                    break;
                default:
                    question = ""; // 不会发生
            }
        } while (answer == 0);

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
