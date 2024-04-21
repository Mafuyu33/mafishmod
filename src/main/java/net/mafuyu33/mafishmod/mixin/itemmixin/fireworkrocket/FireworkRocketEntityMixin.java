package net.mafuyu33.mafishmod.mixin.itemmixin.fireworkrocket;


import net.mafuyu33.mafishmod.mixinhelper.FireworkRocketEntityMixinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {
	@Inject(method = "onEntityHit", at = @At("HEAD"))
	private void init(EntityHitResult entityHitResult, CallbackInfo ci) {
		Entity entity = entityHitResult.getEntity();

		FireworkRocketEntityMixinHelper.storeEntityValue(entity.getId(),5);


//        FireworkRocketEntityMixinHelper.setEntity(entity);//获取这个实体
//        FireworkRocketEntityMixinHelper.setValue(5);//设置循环次数
	}
}