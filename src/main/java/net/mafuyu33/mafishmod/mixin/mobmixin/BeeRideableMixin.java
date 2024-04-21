package net.mafuyu33.mafishmod.mixin.mobmixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

/*
为蜜蜂添加一个骑乘动作
 */
@Mixin(BeeEntity.class)
public abstract class BeeRideableMixin extends AnimalEntity {
   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   @Override
   public ActionResult interactMob(PlayerEntity player, Hand hand){
      var itemStack = player.getStackInHand(hand);
      if (!itemStack.isEmpty()){
         return super.interactMob(player, hand);
      }
      if (this.getFirstPassenger() != null){
         return super.interactMob(player, hand);
      }

      BeeEntity entity = (BeeEntity)(Object)this;

      var isServerSide = !entity.getWorld().isClient();
      if (isServerSide){
         LOGGER.info("some one just interacting a bee");
         player.sendMessage(Text.literal("riding bee!"), false);
         player.setYaw(entity.getYaw());
         player.setPitch(entity.getPitch());
         player.startRiding(entity);
      }

      return ActionResult.success(isServerSide);
   }

   @Override
   public LivingEntity getControllingPassenger(){
      var passenger = this.getFirstPassenger();
      if (passenger instanceof LivingEntity){
         return (LivingEntity)passenger;
      }else {
         return null;
      }
   }
   @Override
   protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
      this.setRotation(controllingPlayer.getYaw(), controllingPlayer.getPitch() * 0.5f);
      this.bodyYaw = this.headYaw = this.getYaw();
      this.prevYaw = this.headYaw;
      // 获取玩家的视角方向
      Vec3d lookDirection = controllingPlayer.getRotationVector();
      // 将玩家的视角方向作为速度向量的一部分
      Vec3d velocity = new Vec3d(lookDirection.x, lookDirection.y, lookDirection.z).normalize();
      // 设置实体的速度
      addVelocity(velocity.multiply(0.05));
      LOGGER.info("" + this.getMovementSpeed());
   }

   @Override
   protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
      return super.getControlledMovementInput(controllingPlayer, movementInput);
   }

   public BeeRideableMixin(EntityType<? extends AnimalEntity> e, World w){
      super(e, w);
      throw new RuntimeException("no construct for mixin class");
   }
}
