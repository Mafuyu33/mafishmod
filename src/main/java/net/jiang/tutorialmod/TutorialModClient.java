package net.jiang.tutorialmod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.jiang.tutorialmod.entity.ModEntities;
//import net.jiang.tutorialmod.util.ModModelPredicateProvider;
import net.jiang.tutorialmod.event.KeyInputHandler;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.CrossbowPosing;

public class TutorialModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.TNT_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STONE_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FU_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIREWORK_ARROW, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DIAMOND_PROJECTILE, FlyingItemEntityRenderer::new);

        KeyInputHandler.register();

//        ModModelPredicateProvider.registerModModels();
    }
}
