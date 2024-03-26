package net.mafuyu33.mafishmod.particle;

import net.mafuyu33.mafishmod.TutorialMod;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

public class StateSaverAndLoader extends PersistentState {
    public List<ParticleInfo> particles = new ArrayList<>();

    public static class ParticleInfo {
        public Vec3d position;
        public double[] color;
        public UUID uuid;

        public ParticleInfo(Vec3d position, double[] color, UUID uuid) {
            this.position = position;
            this.color = color;
            this.uuid = uuid;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParticleInfo that = (ParticleInfo) o;
            return Objects.equals(position, that.position) &&
                    Arrays.equals(color, that.color) &&
                    Objects.equals(uuid, that.uuid);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(position, uuid);
            result = 31 * result + Arrays.hashCode(color);
            return result;
        }
    }

    public Integer totalDirtBlocksBroken = 0;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // 保存粒子信息到NBT
        NbtList particlesList = new NbtList();
        for (ParticleInfo particle : particles) {
            NbtCompound particleNbt = new NbtCompound();
            particleNbt.putUuid("UUID", particle.uuid);
            particleNbt.putDouble("PosX", particle.position.x);
            particleNbt.putDouble("PosY", particle.position.y);
            particleNbt.putDouble("PosZ", particle.position.z);
            particleNbt.putDouble("ColorR", particle.color[0]);
            particleNbt.putDouble("ColorG", particle.color[1]);
            particleNbt.putDouble("ColorB", particle.color[2]);

            particlesList.add(particleNbt);
        }
        nbt.put("Particles", particlesList);

        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound nbt) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.totalDirtBlocksBroken = nbt.getInt("totalDirtBlocksBroken");

        // 读取粒子信息
        NbtList particlesList = nbt.getList("Particles", 10); // NBT的Compound标签类型为10
        for (int i = 0; i < particlesList.size(); i++) {
            NbtCompound particleNbt = particlesList.getCompound(i);
            UUID uuid = particleNbt.getUuid("UUID");
            Vec3d position = new Vec3d(
                    particleNbt.getDouble("PosX"),
                    particleNbt.getDouble("PosY"),
                    particleNbt.getDouble("PosZ"));
            double[] color = {
                    particleNbt.getDouble("ColorR"),
                    particleNbt.getDouble("ColorG"),
                    particleNbt.getDouble("ColorB")
            };

            // 将读取的粒子信息添加到列表中
            state.particles.add(new ParticleInfo(position, color, uuid));
        }

        return state;
    }

    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new, // 若不存在 'StateSaverAndLoader' 则创建
            StateSaverAndLoader::createFromNbt, // 若存在 'StateSaverAndLoader' NBT, 则调用 'createFromNbt' 传入参数
            null // 此处理论上应为 'DataFixTypes' 的枚举，但我们直接传递为空(null)也可以
    );

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        // (注：如需在任意维度生效，请使用 'World.OVERWORLD' ，不要使用 'World.END' 或 'World.NETHER')
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // 当第一次调用了方法 'getOrCreate' 后，它会创建新的 'StateSaverAndLoader' 并将其存储于  'PersistentStateManager' 中。
        //  'getOrCreate' 的后续调用将本地的 'StateSaverAndLoader' NBT 传递给 'StateSaverAndLoader::createFromNbt'。
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, TutorialMod.MOD_ID);

        // 若状态未标记为脏(dirty)，当 Minecraft 关闭时， 'writeNbt' 不会被调用，相应地，没有数据会被保存。
        // 从技术上讲，只有在事实上发生数据变更时才应当将状态标记为脏(dirty)。
        // 但大多数开发者和模组作者会对他们的数据未能保存而感到困惑，所以不妨直接使用 'markDirty' 。
        // 另外，这只将对应的布尔值设定为 TRUE，代价是文件写入磁盘时模组的状态不会有任何改变。(这种情况非常少见)
        state.markDirty();

        return state;
    }
    public void removeParticleByPosition(Vec3d targetPosition) {
        // 移除这个元素
        particles.removeIf(particle -> arePositionsEqual(particle.position, targetPosition));
    }


    public void removeParticleByUuid(UUID uuid) {
        // 使用迭代器遍历，以便在遍历时安全删除元素
        particles.removeIf(particle -> particle.uuid.equals(uuid));
    }
    public void spawnAllParticles(ClientWorld world, MinecraftServer server) {
        System.out.println("生成所有粒子");
        // 获取StateSaverAndLoader实例，确保它已经从NBT加载了数据
        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        if (state.particles.isEmpty()) {
            System.out.println("没有找到粒子信息");
            return;
        }

        // 遍历StateSaverAndLoader中存储的所有粒子信息
        for (StateSaverAndLoader.ParticleInfo particleInfo : state.particles) {
            // 从ParticleInfo对象中获取位置和颜色
            Vec3d position = particleInfo.position;
            double[] color = particleInfo.color;
//            UUID uuid = particleInfo.uuid;
//
//            // 打印粒子信息
//            System.out.println("粒子UUID: " + uuid);
//            System.out.println("位置: " + position);
//            System.out.printf("颜色: R=%.2f, G=%.2f, B=%.2f%n", color[0], color[1], color[2]);
//            System.out.println("---------------------------");

            // 使用Minecraft的粒子系统生成粒子
            world.addParticle(ModParticles.CITRINE_PARTICLE, position.x, position.y, position.z, color[0], color[1], color[2]);
        }
    }
    public boolean arePositionsEqual(Vec3d pos1, Vec3d pos2) {
        return pos1.x == pos2.x && pos1.y == pos2.y && pos1.z == pos2.z;
    }

}
