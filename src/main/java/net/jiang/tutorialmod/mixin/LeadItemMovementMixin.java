package net.jiang.tutorialmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathAwareEntity.class)
public abstract class LeadItemMovementMixin extends MobEntity {
	protected LeadItemMovementMixin(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}
	@Shadow protected abstract void updateForLeashLength(float leashLength);
	@Shadow protected boolean shouldFollowLeash() {
		return true;
	};
	@Shadow public abstract boolean isPanicking();
	@Shadow protected double getFollowLeashSpeed() {
		return 1.0;
	};

	/**
	 * @author Mafish
	 * @reason Make lead stronger
	 */
	@Overwrite
	public void updateLeash() {
		super.updateLeash();
		Entity entity = this.getHoldingEntity();
		if (entity != null && entity.getWorld() == this.getWorld()) {
			this.setPositionTarget(entity.getBlockPos(), 5);
			float f = this.distanceTo(entity);
			if ((PathAwareEntity) (Object) this instanceof TameableEntity) {
				if(((TameableEntity)(Object)this).isInSittingPose()){
					if (f > 100000.0F) {
						this.detachLeash(true, true);
					}

					return;
				}
			}

			this.updateForLeashLength(f);
			if (f > 100000.0F) {
				this.detachLeash(true, true);
				this.goalSelector.disableControl(Goal.Control.MOVE);
			} else if (f > 6.0F) {
				double d = (entity.getX() - this.getX()) / (double)f;
				double e = (entity.getY() - this.getY()) / (double)f;
				double g = (entity.getZ() - this.getZ()) / (double)f;
				this.setVelocity(this.getVelocity().add(Math.copySign(d * d * 0.4, d), Math.copySign(e * e * 0.4, e), Math.copySign(g * g * 0.4, g)));
				this.limitFallDistance();
			} else if (this.shouldFollowLeash() && !this.isPanicking()) {
				this.goalSelector.enableControl(Goal.Control.MOVE);
				float h = 2.0F;
				Vec3d vec3d = (new Vec3d(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ())).normalize().multiply((double)Math.max(f - 2.0F, 0.0F));
				this.getNavigation().startMovingTo(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z, this.getFollowLeashSpeed());
			}
		}
	}

}