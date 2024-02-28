package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.mixinhelper.FearMixinHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Targeter;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeter {
    @Shadow @Final protected GoalSelector goalSelector;
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo ci) {
        // 检查生物的 goalSelector 列表中是否包含 fleeFromPlayerGoal
        boolean containsFleeGoal = false;
        for (PrioritizedGoal goal : this.goalSelector.getGoals()) {
            if (goal.getGoal() == fleeFromPlayerGoal) {
                containsFleeGoal = true;
                break;
            }
        }

        if(this.getAttacker() instanceof PlayerEntity){//让生物恐惧玩家的效果
            UUID uuid1 = this.getUuid();
            int times = FearMixinHelper.getEntityValue(uuid1);
            if (FearMixinHelper.getIsAttacked(uuid1)) {//计时器
                FearMixinHelper.storeEntityValue(uuid1,times-1);
                System.out.println(times);
//                // 输出生物的 goalSelector 列表
//                System.out.println("All goals in goalSelector:");
//                for (PrioritizedGoal goal : this.goalSelector.getGoals()) {
//                    System.out.println(goal.getGoal());
//                }
            }
            if(times > 0 && FearMixinHelper.getIsFirstTime(uuid1) && !containsFleeGoal){//第一次，设置目标
                addFleeFromGoal();
                FearMixinHelper.setIsFirstTime(uuid1,false);
            }

            if(FearMixinHelper.getIsAttacked(uuid1) && times == 0){//停止计时的时候，清除Goal
                removeFleeFromGoal();
                FearMixinHelper.storeIsAttacked(uuid1,false);

                // 输出生物的 goalSelector 列表
                System.out.println("All goals in goalSelector:");
                for (PrioritizedGoal goal : this.goalSelector.getGoals()) {
                    System.out.println(goal.getGoal());
                }
            }
        }
    }
    @Unique
    private Goal fleeFromPlayerGoal;
    // 添加逃离玩家的行为目标
    @Unique
    private void addFleeFromGoal() {
//        System.out.println("add");
//        this.goalSelector.add(1, new FleeEntityGoal<>((PathAwareEntity) (Object) this, PlayerEntity.class, 6.0F, 1.0, 1.2));
        System.out.println("add");
        fleeFromPlayerGoal = new FleeEntityGoal<>((PathAwareEntity) (Object) this, PlayerEntity.class, 6.0F, 1.0, 1.2);
        this.goalSelector.add(1, fleeFromPlayerGoal);
    }

    // 移除逃离猫的行为目标
    @Unique
    private void removeFleeFromGoal() {
        // 遍历并移除逃离猫的行为目标
        System.out.println("remove");
        this.goalSelector.remove(fleeFromPlayerGoal);
    }
}