package net.jiang.tutorialmod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.jiang.tutorialmod.block.ModBlocks;
import net.jiang.tutorialmod.entity.ModEntities;
//import net.jiang.tutorialmod.util.ModModelPredicateProvider;
import net.jiang.tutorialmod.event.KeyInputHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class TutorialModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.TNT_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STONE_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FU_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIREWORK_ARROW, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DIAMOND_PROJECTILE, FlyingItemEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTATO_TNT_PREPARE, RenderLayer.getCutout());

        KeyInputHandler.register();

//        ModModelPredicateProvider.registerModModels();
    }
}
