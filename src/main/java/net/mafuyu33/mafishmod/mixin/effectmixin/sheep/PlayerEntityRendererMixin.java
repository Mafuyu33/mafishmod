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

			// Sync biped information for stuff like bow drawing animation
//			if(sheepRenderer instanceof BipedEntityRenderer) {
//				identity_setBipedIdentityModelPose((AbstractClientPlayerEntity) player, sheep, (BipedEntityRenderer) sheepRenderer);
//			}

			sheepRenderer.render(sheep, f, g, matrixStack, vertexConsumerProvider, i);
		} else {
			super.render((AbstractClientPlayerEntity) player, f, g, matrixStack, vertexConsumerProvider, i);
		}
	}

	@Unique
	private void identity_setBipedIdentityModelPose(AbstractClientPlayerEntity player, LivingEntity identity, LivingEntityRenderer sheepRenderer) {
		BipedEntityModel<?> identityBipedModel = (BipedEntityModel) sheepRenderer.getModel();

		if (identity.isSpectator()) {
			identityBipedModel.setVisible(false);
			identityBipedModel.head.visible = true;
			identityBipedModel.hat.visible = true;
		} else {
			identityBipedModel.setVisible(true);
			identityBipedModel.hat.visible = player.isPartVisible(PlayerModelPart.HAT);
			identityBipedModel.sneaking = identity.isInSneakingPose();

			BipedEntityModel.ArmPose mainHandPose = getArmPose(player, Hand.MAIN_HAND);
			BipedEntityModel.ArmPose offHandPose = getArmPose(player, Hand.OFF_HAND);

			if (mainHandPose.isTwoHanded()) {
				offHandPose = identity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
			}

			if (identity.getMainArm() == Arm.RIGHT) {
				identityBipedModel.rightArmPose = mainHandPose;
				identityBipedModel.leftArmPose = offHandPose;
			} else {
				identityBipedModel.rightArmPose = offHandPose;
				identityBipedModel.leftArmPose = mainHandPose;
			}
		}
	}

}