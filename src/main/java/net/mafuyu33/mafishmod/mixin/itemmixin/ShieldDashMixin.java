package net.mafuyu33.mafishmod.mixin.itemmixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.item.custom.ColliableItem;
import net.mafuyu33.mafishmod.mixinhelper.ShieldDashMixinHelper;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ShieldDashMixin extends Entity implements Attackable {

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean isBlocking();

    @Shadow public abstract boolean isAlive();

    @Unique
    int shieldDashCoolDown = 0;

    public ShieldDashMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isCollidable() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {

        if(getWorld().isClient && this.isBlocking()
                && ShieldDashMixinHelper.isAttackKeyPressed() && shieldDashCoolDown<=0){//盾牌猛击冲刺部分
            // 获取玩家当前面朝的方向
            Vec3d playerLookDirection = this.getRotationVector().normalize();

            if (playerLookDirection.y < 0) {// 如果玩家面朝的是 y 轴负方向，则将 y 分量设为零
                playerLookDirection = new Vec3d(playerLookDirection.x, 0.0, playerLookDirection.z).normalize();
            }else {// 如果玩家面朝的是 y 轴正方向，则将 y 分量减小
                playerLookDirection = new Vec3d(playerLookDirection.x, playerLookDirection.y*0.3, playerLookDirection.z).normalize();
            }
            // 设置速度大小，例如 0.1 表示速度的大小为 0.1 个单位
            double speed = 2;
            // 乘以速度系数
            playerLookDirection = playerLookDirection.multiply(speed);
            // 给玩家应用速度
            this.addVelocity(playerLookDirection);
            getWorld().playSound(this,this.getBlockPos(),
                    SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS,1f,1f);
            shieldDashCoolDown=20;
        }
        if(getWorld().isClient && shieldDashCoolDown>0){//盾牌猛击内置冷却部分，传递数据包到服务端
            shieldDashCoolDown--;
//            System.out.println(shieldDashCoolDown);

            sentC2S();
        }
        if(this.isPlayer() && isBlocking()) {//盾牌猛击造成伤害和击退部分
            if(checkPlayerCollisions((PlayerEntity) (Object) this) != null) {
                Entity entity = checkPlayerCollisions((PlayerEntity) (Object) this);
//                System.out.println(ShieldDashMixinHelper.getHitCoolDown(this.getId()));
                if(ShieldDashMixinHelper.getHitCoolDown(this.getId())>=15) {//盾牌冲刺中
                    Vec3d playerLookDirection = this.getRotationVector().normalize();
                    playerLookDirection = new Vec3d(playerLookDirection.x, 0, playerLookDirection.z).normalize();
                    double speed = 0.5;
                    // 乘以速度系数
                    Vec3d upVector = new Vec3d(0, 0.1, 0);
                    playerLookDirection = playerLookDirection.multiply(speed).add(upVector);
                    entity.damage(getDamageSources().playerAttack((PlayerEntity) (Object) this),5f);
                    entity.addVelocity(playerLookDirection);
                }
            }
        }
    }

    @Unique
    @Environment(EnvType.CLIENT)
    private void sentC2S(){
        PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
        buf.writeInt(shieldDashCoolDown);
        ClientPlayNetworking.send(ModMessages.Shield_Dash_ID, buf);
    }
    @Unique
    private Entity checkPlayerCollisions(PlayerEntity player) {

        World world = player.getEntityWorld();
        Box collisionBox = player.getBoundingBox().expand(2.0); // 检测范围为玩家周围 2 格的立方体

        // 获取与玩家碰撞的所有实体
        for (Entity entity : world.getOtherEntities(player, collisionBox)) {
            // 确保碰撞到的实体不是玩家自身，并且是生物实体
            if (entity != player && entity instanceof LivingEntity) {
                return entity;
//                System.out.println("储存生物id");
//                System.out.println("HitCoolDown"+ ShieldDashMixinHelper.getHitCoolDown(entity.getId()));
//                System.out.println("isHit"+ ShieldDashMixinHelper.getEntityValue(entity.getId()));
            }
        }
        return null;
    }
}
