package net.jiang.tutorialmod.particle;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ParticleStorage {
    private static final Map<Vec3d, Integer> positionToIdMap = new HashMap<>();
    private static final Map<Integer, double[]> particleColor = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger();

    public void addParticle(Vec3d position, double red, double green, double blue) {
        int id = nextId.getAndIncrement();
        positionToIdMap.put(position,id);
        particleColor.put(id, new double[]{red, green, blue});

    }

    public void removeParticleAtPosition(Vec3d position) {
        Integer particleId = positionToIdMap.remove(position);
        if (particleId != null) {
            particleColor.remove(particleId);
        }
    }
    public boolean hasParticleAtPosition(Vec3d position) {
        return positionToIdMap.containsKey(position);
    }

    public void clearAll() {
        positionToIdMap.clear();
        particleColor.clear();
    }

    public static void spawnAllParticles(ClientWorld world) {
        positionToIdMap.forEach((position, id) -> {
            double[] color = particleColor.get(id);
            double red = color[0];
            double green = color[1];
            double blue = color[2];
            world.addParticle(ModParticles.CITRINE_PARTICLE, position.x, position.y, position.z, red, green, blue);
        });
    }
}


