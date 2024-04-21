package net.mafuyu33.mafishmod.mixin.itemmixin.lead;

import net.mafuyu33.mafishmod.mixinhelper.FearMixinHelper;
import net.minecraft.entity.Entity;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeter {
    @Shadow @Final protected GoalSelector goalSelector;

    @Shadow @Nullable public abstract Entity getHoldingEntity();
    @Shadow private Entity holdingEntity;

    @Shadow public abstract void detachLeash(boolean sendPacket, boolean dropItem);
    @Shadow public abstract boolean isLeashed();

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author
     * Mafish
     * @reason
     * 所有生物都能被拴绳拴住
     * Make every mob can be leashed
     */
    @Overwrite public boolean canBeLeashedBy(PlayerEntity player){
        return !this.isLeashed();
    }



    @Inject(at = @At("HEAD"), method = "interactWithItem", cancellable = true)
    private void init(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {//拴绳增强
        ItemStack itemStack =player.getStackInHand(hand);
        // 检测范围为玩家周围 50 格的立方体
        Box collisionBox = player.getBoundingBox().expand(50.0);
        for (Entity entity : getWorld().getOtherEntities(player, collisionBox)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (!itemStack.isOf(Items.LEAD) && ((MobEntity) livingEntity).getHoldingEntity() != null
                        && ((MobEntity) livingEntity).getHoldingEntity() == player) {
                    // 玩家手中牵着实体并且点击一个没有被拴着的实体，就让两个实体互相连接.
                    if(!getWorld().isClient) {
                        ((MobEntity) entity).attachLeash(this, true);
                    }
//                    if(!getWorld().isClient) {
//                        this.detachLeashWithoutClearNbt(true, false);
//                    }
                    cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo ci) {
        boolean containsFleeGoal = false;
        for (PrioritizedGoal goal : this.goalSelector.getGoals()) {// 检查生物的 goalSelector 列表中是否包含 fleeFromPlayerGoal
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
    public void attachLeashWithoutClearNbt(Entity entity, boolean sendPacket) {//不清除nbt的拴住方法
        this.holdingEntity = entity;
        if (!this.getWorld().isClient && sendPacket && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).getChunkManager().sendToOtherNearbyPlayers(this, new EntityAttachS2CPacket(this, this.holdingEntity));
        }

        if (this.hasVehicle()) {
            this.stopRiding();
        }

    }

    @Unique
    public void detachLeashWithoutClearNbt(boolean sendPacket, boolean dropItem) {
        if (this.holdingEntity != null) {
            this.holdingEntity = null;



            if (!this.getWorld().isClient && dropItem) {
                this.dropItem(Items.LEAD);
            }

            if (!this.getWorld().isClient && sendPacket && this.getWorld() instanceof ServerWorld) {
                ((ServerWorld)this.getWorld()).getChunkManager().sendToOtherNearbyPlayers(this, new EntityAttachS2CPacket(this, (Entity)null));
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