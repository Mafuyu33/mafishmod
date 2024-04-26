package net.mafuyu33.mafishmod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.mafuyu33.mafishmod.block.ModBlocks;
import net.mafuyu33.mafishmod.entity.ModEntities;
//import net.mafuyu33.mafishmod.util.ModModelPredicateProvider;
import net.mafuyu33.mafishmod.event.AttackKeyCheckHandler;
import net.mafuyu33.mafishmod.event.ChatMessageHandler;
import net.mafuyu33.mafishmod.event.KeyInputHandler;
import net.mafuyu33.mafishmod.mixinhelper.BellBlockDelayMixinHelper;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.mafuyu33.mafishmod.particle.ModParticles;
import net.mafuyu33.mafishmod.particle.StateSaverAndLoader;
import net.mafuyu33.mafishmod.particle.custom.CitrineParticle;
import net.mafuyu33.mafishmod.particle.custom.KnockBackParticle;
import net.mafuyu33.mafishmod.particle.custom.RubberParticle;
import net.mafuyu33.mafishmod.render.ALeaf;
import net.mafuyu33.mafishmod.screen.GemPolishingScreen;
import net.mafuyu33.mafishmod.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class TutorialModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //投射物注册
        EntityRendererRegistry.register(ModEntities.TNT_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STONE_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FU_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIREWORK_ARROW, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DIAMOND_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.LIGHTNING_PROJECTILE, FlyingItemEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTATO_TNT_PREPARE, RenderLayer.getCutout());

        KeyInputHandler.register();
        ChatMessageHandler.register();
        AttackKeyCheckHandler.registerAttackKeyListener();
        HudRenderCallback.EVENT.register(new ALeaf());

        HandledScreens.register(ModScreenHandlers.GEM_POLISHING_SCREEN_HANDLER, GemPolishingScreen::new);
        ModMessages.registerS2CPackets();

        ParticleFactoryRegistry.getInstance().register(ModParticles.CITRINE_PARTICLE, CitrineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.RUBBER_PARTICLE, RubberParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.KNOCK_BACK_PARTICLE, KnockBackParticle.Factory::new);

//        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, Blocks.CHEST);

//        ModModelPredicateProvider.registerModModels();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {//客户端已经成功连接到服务器
                // 调用您的方法
                StateSaverAndLoader.getServerState(client.getServer()).spawnAllParticles(client.world,client.getServer());
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler,client) -> {//客户端已经成功连接到服务器
            // 清空hashmap
            BellBlockDelayMixinHelper.BellBlockEntityMap.clear();
            BellBlockDelayMixinHelper.HitCoolDownMap.clear();
            BellBlockDelayMixinHelper.DirectionMap.clear();
        });
    }
}
