package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.luoyangshovel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
	public FallingBlockEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		FallingBlockEntity fallingBlockEntity = (FallingBlockEntity) (Object) this;
		Box boundingBox = fallingBlockEntity.getBoundingBox();

		// 获取世界中的所有实体
		List<Entity> entities = fallingBlockEntity.getWorld().getOtherEntities(fallingBlockEntity, boundingBox);

		// 遍历这些实体并检查是否为生物实体
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity livingEntity) {
				// 检查碰撞箱是否重叠
				if (boundingBox.intersects(livingEntity.getBoundingBox())) {
					// 对生物实体造成伤害
					DamageSource damageSource = getDamageSources().fallingBlock(fallingBlockEntity);
					livingEntity.damage(damageSource, fallingBlockEntity.fallHurtAmount);
				}
			}
		}
	}
}