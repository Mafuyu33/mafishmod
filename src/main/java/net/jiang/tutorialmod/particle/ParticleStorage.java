package net.jiang.tutorialmod.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static net.jiang.tutorialmod.TutorialMod.LOGGER;

public class ParticleStorage {
//    private static final Map<String, ParticleStorage> saveParticleStorageMap = new HashMap<>();
private static final Map<String, ParticleStorage> saveParticleStorageMap = new HashMap<>();
    private final Map<Vec3d, Integer> positionToIdMap = new HashMap<>();
    private final Map<Integer, double[]> particleColor = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger();

    // 私有构造函数，确保只能通过静态方法获取实例
    private ParticleStorage() {
    }

    // 获取指定世界的 ParticleStorage 实例，如果不存在则创建一个新的
    public static ParticleStorage getOrCreateForWorld() {//用存档名字
        String saveName = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getSaveProperties().getLevelName();
//        printParticleStorageMapContents();
        return saveParticleStorageMap.computeIfAbsent(saveName, k -> new ParticleStorage());
    }

    public static void printParticleStorageMapContents() {
        System.out.println("Contents of saveParticleStorageMap:");
        for (Map.Entry<String, ParticleStorage> entry : saveParticleStorageMap.entrySet()) {
            String saveName = entry.getKey();
            ParticleStorage particleStorage = entry.getValue();
            System.out.println("Save Name: " + saveName + ", ParticleStorage: " + particleStorage.toString());
            // 如果你希望打印 ParticleStorage 的更多信息，可以添加相应的打印语句
        }
    }

    public void printPositionToIdMapContents() {
        System.out.println("Contents of positionToIdMap:");
        for (Map.Entry<Vec3d, Integer> entry : positionToIdMap.entrySet()) {
            Vec3d position = entry.getKey();
            Integer id = entry.getValue();
            System.out.println("Position: " + position.toString() + ", Id: " + id);
        }
    }
    public void addParticle(Vec3d position, double red, double green, double blue) {
        int id = nextId.getAndIncrement();
        positionToIdMap.put(position, id);
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

    public void spawnAllParticles(ClientWorld world) {
//        printPositionToIdMapContents();
        positionToIdMap.forEach((position, id) -> {
            double[] color = particleColor.get(id);
            double red = color[0];
            double green = color[1];
            double blue = color[2];
            world.addParticle(ModParticles.CITRINE_PARTICLE, position.x, position.y, position.z, red, green, blue);
        });
    }

    public Vec3d findCollidedParticlePosition(Box collisionBox) {
        for (Vec3d position : positionToIdMap.keySet()) {
            if (isParticleInsideBox(position, collisionBox)) {
                return position;
            }
        }
        return null; // 没有找到碰撞的粒子
    }

    private boolean isParticleInsideBox(Vec3d position, Box collisionBox) {
        double posX = position.x;
        double posY = position.y;
        double posZ = position.z;
        double boxMinX = collisionBox.minX;
        double boxMinY = collisionBox.minY;
        double boxMinZ = collisionBox.minZ;
        double boxMaxX = collisionBox.maxX;
        double boxMaxY = collisionBox.maxY;
        double boxMaxZ = collisionBox.maxZ;

        return posX >= boxMinX && posX <= boxMaxX
                && posY >= boxMinY && posY <= boxMaxY
                && posZ >= boxMinZ && posZ <= boxMaxZ;
    }
}








//package net.jiang.tutorialmod.particle;
//
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.util.math.Box;
//import net.minecraft.util.math.Vec3d;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class ParticleStorage {
//    private static final Map<Vec3d, Integer> positionToIdMap = new HashMap<>();
//    private static final Map<Integer, double[]> particleColor = new HashMap<>();
//    private final AtomicInteger nextId = new AtomicInteger();
//
//    public void addParticle(Vec3d position, double red, double green, double blue) {
//        int id = nextId.getAndIncrement();
//        positionToIdMap.put(position,id);
//        particleColor.put(id, new double[]{red, green, blue});
//
//    }
//
//    public static void removeParticleAtPosition(Vec3d position) {
//        Integer particleId = positionToIdMap.remove(position);
//        if (particleId != null) {
//            particleColor.remove(particleId);
//        }
//    }
//    public static boolean hasParticleAtPosition(Vec3d position) {
//        return positionToIdMap.containsKey(position);
//    }
//
//    public void clearAll() {
//        positionToIdMap.clear();
//        particleColor.clear();
//    }
//
//    public static void spawnAllParticles(ClientWorld world) {
//        positionToIdMap.forEach((position, id) -> {
//            double[] color = particleColor.get(id);
//            double red = color[0];
//            double green = color[1];
//            double blue = color[2];
//            world.addParticle(ModParticles.CITRINE_PARTICLE, position.x, position.y, position.z, red, green, blue);
//        });
//    }
//    // 寻找与给定碰撞箱相交的粒子，并返回其位置
//    public static Vec3d findCollidedParticlePosition(Box collisionBox) {
//        for (Vec3d position : positionToIdMap.keySet()) {
//            if (isParticleInsideBox(position, collisionBox)) {
////                System.out.println(position);
//                return position;
//            }
//        }
//        return null; // 没有找到碰撞的粒子
//    }
//
//    // 检查给定位置的粒子是否在碰撞箱内
//    private static boolean isParticleInsideBox(Vec3d position, Box collisionBox) {
//        double posX = position.x;
//        double posY = position.y;
//        double posZ = position.z;
//        double boxMinX = collisionBox.minX;
//        double boxMinY = collisionBox.minY;
//        double boxMinZ = collisionBox.minZ;
//        double boxMaxX = collisionBox.maxX;
//        double boxMaxY = collisionBox.maxY;
//        double boxMaxZ = collisionBox.maxZ;
//
//        return posX >= boxMinX && posX <= boxMaxX
//                && posY >= boxMinY && posY <= boxMaxY
//                && posZ >= boxMinZ && posZ <= boxMaxZ;
//    }
//}
//
//
