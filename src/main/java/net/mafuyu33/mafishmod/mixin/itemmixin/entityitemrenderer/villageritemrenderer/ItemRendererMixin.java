package net.mafuyu33.mafishmod.mixin.itemmixin.entityitemrenderer.villageritemrenderer;

import net.mafuyu33.mafishmod.TutorialMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "renderItem*", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        // 检查是否是特定物品
        if (Registries.ITEM.getId(stack.getItem()).equals(new Identifier(TutorialMod.MOD_ID, "villager_item"))) {
            // 取消默认渲染
            ci.cancel();

            // 渲染生物模型，例如羊驼
            VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER, mc.world);
            matrices.push();
            // 使用 org.joml.Quaternionf 进行旋转
//            Quaternionf rotation = new Quaternionf().rotateY((float) Math.toRadians(180));
//            matrices.multiply(rotation);

            matrices.scale(0.5F, 0.5F, 0.5F);// 调整缩放比例
            mc.getEntityRenderDispatcher().render(villager, 0, 0, 0, 0.0F, 1.0F, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}