package net.mafuyu33.mafishmod.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.mafuyu33.mafishmod.event.ChatMessageHandler;
import net.mafuyu33.mafishmod.item.custom.ColliableItem;
import net.mafuyu33.mafishmod.item.custom.MathSwordItem;
import net.mafuyu33.mafishmod.mixinhelper.MathQuestionMixinHelper;
import net.mafuyu33.mafishmod.mixinhelper.ShieldDashMixinHelper;
import net.mafuyu33.mafishmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean isDead();

    @Shadow public abstract Hand getActiveHand();

    @Shadow public abstract boolean isBlocking();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean isAlive();

    @Unique
    int shieldDashCoolDown = 0;
    @Unique
    int time = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isCollidable() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {

//        if(!getWorld().isClient) {//想办法让碰到粒子的生物（除了玩家被击退+受伤）
//            Vec3d[] pos = ParticleStorage.getOrCreateForWorld().findParticlesWithColor(1.0, 1.0, 0.0);
//            if (pos.length > 0) {
//                double damage = 1.0; // 设置造成的伤害值
//                for (Vec3d position : pos) {//For循环嵌套，每个pos都检测一边
//                    System.out.println(position);
//                    applyEffectsToNearbyEntities((LivingEntity) (Object) this, position, damage);
//                }
//            }
//        }

        if(this.getType()==EntityType.VILLAGER) {//村民恭喜发财
            int entityId = this.getId();// 获取实体的ID
            int times = WeaponEnchantmentMixinHelper.getEntityValue(entityId);
            if (times > 0) {
                WeaponEnchantmentMixinHelper.storeEntityValue(entityId, times - 1);
                this.dropItem(Items.EMERALD);
            }
        }

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

            PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
            buf.writeInt(shieldDashCoolDown);
            ClientPlayNetworking.send(ModMessages.Shield_Dash_ID, buf);
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

        if(WeaponEnchantmentMixinHelper.getReverse(this.getUuid())==1){//反转了，提供向上速度
            addVelocity(0,0.1,0);
        }


        if(this.isPlayer() && this.hasStatusEffect(ModStatusEffects.TELEPORT_EFFECT)) {//传送药水
            time++;
            if (time > 1) {
                randomTeleport(this.getWorld(), (LivingEntity) (Object) this);
                time=0;
            }
        }

    }


    @Unique
    private void randomTeleport(World world, LivingEntity user) {
        if (!world.isClient) {
            for(int i = 0; i < 16; ++i) {
                double d = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                double e = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
                double f = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                if (user.hasVehicle()) {
                    user.stopRiding();
                }

                Vec3d vec3d = user.getPos();
                if (user.teleport(d, e, f, true)) {
                    world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
                    SoundCategory soundCategory;
                    SoundEvent soundEvent;
                    if (user instanceof FoxEntity) {
                        soundEvent = SoundEvents.ENTITY_FOX_TELEPORT;
                        soundCategory = SoundCategory.NEUTRAL;
                    } else {
                        soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                        soundCategory = SoundCategory.PLAYERS;
                    }

                    world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), soundEvent, soundCategory);
                    user.onLanding();
                    break;
                }
            }
        }
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
//    @Unique
//    private static void applyEffectsToNearbyEntities(LivingEntity entity, Vec3d pos, double damage) {
//        if (pos == null || entity == null || entity.getWorld() == null) {
//            return;
//        }
//        Box box = new Box(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 0.1, pos.y + 0.1, pos.z + 0.1);
//        // 检查碰撞
//            if (entity.getBoundingBox().intersects(box)) {
//                System.out.println("碰撞!");
//                // 碰到生物时，对其进行击退和受伤
//                if (!entity.isPlayer()) {
//                    System.out.println("造成伤害！");
//                    Vec3d knockBackVec = new Vec3d(entity.getX() - pos.x, entity.getY() - pos.y, entity.getZ() - pos.z).normalize().multiply(-1.0); // 计算击退向量
//                    entity.takeKnockback(1.0f, knockBackVec.x, knockBackVec.z); // 进行击退
//                    entity.damage(entity.getWorld().getDamageSources().magic(), (float) damage); // 造成伤害
//                }
//            }
//    }

}
