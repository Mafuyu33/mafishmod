package net.mafuyu33.mafishmod.mixin.effectmixin.sheep;

import net.fabricmc.fabric.mixin.client.rendering.LivingEntityRendererAccessor;
import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}
	@Shadow
	private static BipedEntityModel.ArmPose getArmPose(AbstractClientPlayerEntity player, Hand hand) {
		return null;
	}

	@Redirect(
			method = "render*",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	)
	private void redirectRender(LivingEntityRenderer renderer, LivingEntity player, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

		LivingEntity sheep = new SheepEntity(EntityType.SHEEP,MinecraftClient.getInstance().world);

		if(player.hasStatusEffect(ModStatusEffects.SHEEP_EFFECT)) {
			EntityRenderer sheepRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(sheep);

			LimbAnimatorAccessor target = (LimbAnimatorAccessor) sheep.limbAnimator;
			LimbAnimatorAccessor source = (LimbAnimatorAccessor) player.limbAnimator;
			target.setPrevSpeed(source.getPrevSpeed());
			target.setSpeed(source.getSpeed());
			target.setPos(source.getPos());
			sheep.handSwinging = player.handSwinging;
			sheep.handSwingTicks = player.handSwingTicks;
			sheep.lastHandSwingProgress = player.lastHandSwingProgress;
			sheep.handSwingProgress = player.handSwingProgress;
			sheep.bodyYaw = player.bodyYaw;
			sheep.prevBodyYaw = player.prevBodyYaw;
			sheep.headYaw = player.headYaw;
			sheep.prevHeadYaw = player.prevHeadYaw;
			sheep.age = player.age;
			sheep.preferredHand = player.preferredHand;
			sheep.setOnGround(player.isOnGround());
			sheep.setVelocity(player.getVelocity());

			sheep.setPose(player.getPose());
			// 将俯仰角度设置为玩家的俯仰角度
			// 将前一帧俯仰角度设置为玩家的前一帧俯仰角度
			sheep.setPitch(player.getPitch());
			sheep.prevPitch = player.prevPitch;

			sheepRenderer.render(sheep, f, g, matrixStack, vertexConsumerProvider, i);
		} else {
			super.render((AbstractClientPlayerEntity) player, f, g, matrixStack, vertexConsumerProvider, i);
		}
	}
}