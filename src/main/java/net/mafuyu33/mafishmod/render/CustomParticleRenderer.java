package net.mafuyu33.mafishmod.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CustomParticleRenderer {
    public static void spawnFlameParticles(Vec3d pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world != null) {
            ParticleEffect flameParticle = ParticleTypes.FLAME;
            BlockPos blockPos = new BlockPos((int) pos.x, (int)pos.y, (int)pos.z);
            // 添加火焰粒子到世界
            world.addParticle(flameParticle, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.0, 0.0, 0.0);
            System.out.println(123);
        }
    }
}

