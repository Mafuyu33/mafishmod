package net.jiang.tutorialmod.particle;

import net.jiang.tutorialmod.TutorialMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.jiang.tutorialmod.TutorialMod.LOGGER;

public class ParticleStorage{
//    private static final Map<String, ParticleStorage> saveParticleStorageMap = new HashMap<>();
private static final Map<String, ParticleStorage> saveParticleStorageMap = new HashMap<>();
    // 使用UUID作为粒子标识符
    private final ConcurrentHashMap<Vec3d, UUID> positionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, double[]> particleColor = new ConcurrentHashMap<>();

    // 私有构造函数，确保只能通过静态方法获取实例
    private ParticleStorage() {
    }

    // 获取指定世界的 ParticleStorage 实例，如果不存在则创建一个新的
    public static ParticleStorage getOrCreateForWorld() {//用存档名字
        String saveName = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getSaveProperties().getLevelName();
//        printParticleStorageMapContents();
        return saveParticleStorageMap.computeIfAbsent(saveName, k -> new ParticleStorage());
    }

//    public static void printParticleStorageMapContents() {
//        System.out.println("Contents of saveParticleStorageMap:");
//        for (Map.Entry<String, ParticleStorage> entry : saveParticleStorageMap.entrySet()) {
//            String saveName = entry.getKey();
//            ParticleStorage particleStorage = entry.getValue();
//            System.out.println("Save Name: " + saveName + ", ParticleStorage: " + particleStorage.toString());
//            // 如果你希望打印 ParticleStorage 的更多信息，可以添加相应的打印语句
//        }
//    }

//    public void printPositionToIdMapContents() {
//        System.out.println("Contents of positionToIdMap:");
//        for (Map.Entry<Vec3d, Integer> entry : positionToIdMap.entrySet()) {
//            Vec3d position = entry.getKey();
//            Integer id = entry.getValue();
//            System.out.println("Position: " + position.toString() + ", Id: " + id);
//        }
//    }
    public void addParticle(Vec3d position, double red, double green, double blue) {
        UUID id = UUID.randomUUID();
        positionMap.put(position,id);
        particleColor.put(id, new double[]{red, green, blue});
    }

    public void removeParticleByPosition(Vec3d position) {
        positionMap.remove(position);
        particleColor.remove(getUUIDByPosition(position));
    }

    public boolean hasParticleAtPosition(Vec3d position) {
        return positionMap.containsKey(position);
    }

    public void clearAll() {
        positionMap.clear();
        particleColor.clear();
    }

    public void spawnAllParticles(ClientWorld world) {
        positionMap.forEach((position,id) -> {
            double[] color = particleColor.get(id);
            world.addParticle(ModParticles.CITRINE_PARTICLE, position.x, position.y, position.z, color[0], color[1], color[2]);
        });
    }
    // 根据粒子的 ID 获取其位置
    public UUID getUUIDByPosition(Vec3d position) {
        // 直接使用UUID作为键来获取位置
        return positionMap.get(position);
    }
//    public Vec3d findCollidedParticlePosition(Box collisionBox) {
//        for (Vec3d position : positionToIdMap.keySet()) {
//            if (isParticleInsideBox(position, collisionBox)) {
//                return position;
//            }
//        }
//        return null; // 没有找到碰撞的粒子
//    }
//    public Vec3d[] findParticlesWithColor(double red, double green, double blue) {
//        List<Vec3d> positions = new ArrayList<>();
//        for (Map.Entry<Vec3d, Integer> entry : positionToIdMap.entrySet()) {
//            Vec3d position = entry.getKey();
//            Integer id = entry.getValue();
//            double[] color = particleColor.get(id);
//            if (color != null && color.length == 3 && color[0] == red && color[1] == green && color[2] == blue) {
//                positions.add(position);
//            }
//        }
//        return positions.toArray(new Vec3d[0]);
//    }

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
