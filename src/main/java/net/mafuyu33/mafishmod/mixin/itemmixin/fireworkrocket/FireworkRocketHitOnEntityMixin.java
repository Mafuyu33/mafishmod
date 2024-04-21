package net.mafuyu33.mafishmod.mixin.itemmixin.fireworkrocket;

import net.mafuyu33.mafishmod.mixinhelper.FireworkRocketEntityMixinHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class FireworkRocketHitOnEntityMixin extends Entity implements Attackable {

	@Shadow @Nullable public abstract LivingEntity getAttacker();

	@Shadow @Nullable private LivingEntity attacker;

	@Shadow public abstract void kill();

	@Shadow public abstract void wakeUp();

	public FireworkRocketHitOnEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	@Unique
	private static final double velocityY = 1.15; // Y轴方向速度，重力0.08
	@Unique
	private static final double INITIAL_VELOCITY = 0.3; // 初始速度
	@Unique
	private static double angle = Math.toRadians(Math.random() * 360); // 随机初始角度
	@Unique
	private static double velocityX = INITIAL_VELOCITY * Math.cos(angle); // 初始X轴速度
	@Unique
	private static double velocityZ = INITIAL_VELOCITY * Math.sin(angle); // 初始Z轴速度
	@Unique
	private int delayCounter = 0; // 延迟计数器
	@Unique
	int ParticleLifes=0;

	// 假设您想在特定事件发生后延迟5秒执行一些操作
	@Unique
	public void triggerDelayedAction() {
		this.delayCounter = 30; // 延迟5秒，假设游戏帧率是每秒20帧
	}
	@Unique
	private static void applyContinuousForce(Entity entity) {


		double velocityMagnitude = Math.sqrt(velocityX*velocityX+velocityZ*velocityZ);
		// 改变角度增量，以改变方向
		double angleIncrement = Math.toRadians(5); // 你可以根据需要调整增量
		angle += angleIncrement;

		// 更新合速度的方向，保持大小不变
		velocityX = velocityMagnitude * Math.cos(angle);
		velocityZ = velocityMagnitude * Math.sin(angle);

		entity.addVelocity(velocityX, velocityY, velocityZ);
	}
	@Unique
	private void explode() {
		this.getWorld().createExplosion( null, this.getX(), this.getY(), this.getZ(),  2.5F, World.ExplosionSourceType.TNT);
	}
	@Unique
	private void addParticles() {


		Vec3d Pos = this.getPos();
		World world =getWorld();
		world.addParticle(ParticleTypes.EXPLOSION,Pos.x,Pos.y,Pos.z,0,0,0);
//			((ServerWorld) world).spawnParticles(new DustParticleEffect
//					(new Vector3f(random.nextFloat(),random.nextFloat(),random.nextFloat()),4.0F),
//					Pos.x,Pos.y,Pos.z,10,2,2,2,0.5);
//			System.out.println(123);
//		System.out.println(123);
//		if (this.getWorld() instanceof ServerWorld world) {
//
//
//				LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
//				if (lightning != null) {
//					lightning.refreshPositionAfterTeleport(this.getPos());
//					world.spawnEntity(lightning);
//				}
//
//
//			world.spawnParticles(new DustParticleEffect(new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()),  4.0F)
//					, this.getX(), this.getY(), this.getZ(),  1,  2,  2,  2,  0.5);
//
//		}


	}
	@Inject(method = "tick", at = @At("HEAD"))
	private void init(CallbackInfo ci) {
		Vec3d Pos=null;
		int entityId = this.getId();// 获取实体的ID
		Entity entity = getWorld().getEntityById(entityId);
		int times = FireworkRocketEntityMixinHelper.getEntityValue(entityId);// 获取与实体ID关联的值
		if(entity != null) {
			Pos = entity.getPos();
		}
		if (times > 0) {
			applyContinuousForce(this);
			FireworkRocketEntityMixinHelper.storeEntityValue(entityId, times - 1);
		}
		if (times == 1) {
			ParticleLifes=30;
			triggerDelayedAction();
		}


		if(ParticleLifes>0 & entity !=null){
			addParticles();
			ParticleLifes--;
		}

		if (this.delayCounter > 0 && entity != null) {
			this.delayCounter--;
			if (this.delayCounter == 0) {
				if(entity instanceof PlayerEntity){
					entity.kill();
					explode();
				}else {
					((LivingEntity) entity).dead = true;
					explode();
					entity.discard();
				}
			}
		}
	}
}