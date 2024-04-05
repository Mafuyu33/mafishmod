//package net.mafuyu33.mafishmod.mixin.canclimb;
//
//import net.mafuyu33.mafishmod.util.WallAttackGoal;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.Targeter;
//import net.minecraft.entity.ai.goal.*;
//import net.minecraft.entity.effect.StatusEffects;
//import net.minecraft.entity.mob.MobEntity;
//import net.minecraft.entity.mob.PathAwareEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.world.World;
//import org.spongepowered.asm.mixin.*;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.function.Predicate;
//
//@Mixin(MobEntity.class)
//public abstract class MobEntityMixin extends LivingEntity implements Targeter {
//    @Shadow @Final protected GoalSelector goalSelector;
//    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
//        super(entityType, world);
//    }
//
//    @Inject(at = @At("HEAD"), method = "tick")
//    private void init(CallbackInfo ci) {
//        if (!this.getWorld().isClient) {
//            // 检查是否存在药水效果
//            boolean hasPotionEffect = this.hasStatusEffect(StatusEffects.SPEED);
//            // 检查是否有药水效果，这里需要您根据具体情况实现
//            // 示例：假设药水效果为 "health_boost"
//            // 如果存在药水效果并且目标列表中没有 wallAttackGoalGoal，则添加目标
//            if (hasPotionEffect && !containsJumpAttackGoal()) {
//                addGoal();
//            }
//            // 如果不存在药水效果并且目标列表中有 wallAttackGoalGoal，则移除目标
//            else if (!hasPotionEffect && containsJumpAttackGoal()) {
//                removeGoal();
//            }
//        }
//    }
//
//    @Unique
//    private Goal wallAttackGoalGoal;
//    // 添加行为目标
//    @Unique
//    private void addGoal() {
//        System.out.println("add");
//        if((MobEntity)(Object)this instanceof PathAwareEntity) {
////            Predicate<Goal> predicate = goal -> goal instanceof Goal;
////            this.goalSelector.clear(predicate);
//            wallAttackGoalGoal = new PounceAtTargetGoal((PathAwareEntity) (Object)this,0.4f);
////            wallAttackGoalGoal = new WallAttackGoal((PathAwareEntity) (Object) this, 1.0F, true);
//            this.goalSelector.add(3, wallAttackGoalGoal);
//            this.goalSelector.add(5, new WanderAroundFarGoal((PathAwareEntity) (Object)this, 0.8));
//            this.goalSelector.add(6, new LookAtEntityGoal((PathAwareEntity) (Object)this, PlayerEntity.class, 8.0f));
//            this.goalSelector.add(6, new LookAroundGoal((PathAwareEntity) (Object)this));
//        }
//    }
//
//    // 移除行为目标
//    @Unique
//    private void removeGoal() {
//        // 遍历并移除行为目标
//        System.out.println("remove");
//        this.goalSelector.remove(wallAttackGoalGoal);
//    }
//
//    // 检查目标列表中是否包含 wallAttackGoalGoal
//    @Unique
//    private boolean containsJumpAttackGoal() {
//        for (PrioritizedGoal goal : this.goalSelector.getGoals()) {
//            if (goal.getGoal() == wallAttackGoalGoal) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
