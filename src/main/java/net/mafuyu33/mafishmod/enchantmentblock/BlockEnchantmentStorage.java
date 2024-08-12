package net.mafuyu33.mafishmod.enchantmentblock;

import net.mafuyu33.mafishmod.ServerManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class BlockEnchantmentStorage{
    public static void addBlockEnchantment(BlockPos blockPos, NbtList enchantments) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 创建 StateSaverAndLoader 实例
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        // 将方块附魔信息添加到列表中
        state.blockEnchantments.put(blockPos, enchantments);
    }
    public static void removeBlockEnchantment(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 获取 BlockStateSaverAndLoader 实例
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);

        // 移除指定位置的方块附魔信息
        state.blockEnchantments.remove(blockPos);
    }

    public static NbtList getEnchantmentsAtPosition(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 获取 BlockStateSaverAndLoader 实例
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);

        // 遍历附魔信息列表，找到指定位置的方块附魔信息
        // 如果没有找到指定位置的方块附魔信息，则返回空列表
        return state.blockEnchantments.getOrDefault(blockPos, new NbtList());
    }

    public static int getLevel(Enchantment enchantment, BlockPos blockPos) {
        // 获取方块的附魔信息
        NbtList enchantments = getEnchantmentsAtPosition(blockPos);

        // 遍历附魔信息
        for (int i = 0; i < enchantments.size(); i++) {
            // 获取单个附魔信息
            NbtCompound enchantmentInfo = enchantments.getCompound(i);

            // 提取附魔名称和等级
            String enchantmentName = enchantmentInfo.getString("id");
            int level = enchantmentInfo.getInt("lvl");

            // 检查附魔名称是否匹配
            if (enchantmentName.equals(Registries.ENCHANTMENT.getId(enchantment).toString())) {
                // 返回附魔等级
                return level;
            }
        }

        // 如果没有找到匹配的附魔信息，默认返回0
        return 0;
    }



}

