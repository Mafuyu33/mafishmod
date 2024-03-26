package net.mafuyu33.mafishmod.mixinhelper;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;


import java.util.HashMap;
import java.util.Map;

import static net.minecraft.enchantment.EnchantmentHelper.*;

public class BlockEnchantmentHelper {
    // 创建一个静态 Map 来存储 BlockPos 和 NbtList enchantments
    private static final Map<BlockPos, NbtList> reverseMap = new HashMap<>();

    // 在适当的时候将 BlockPos 和 NbtList enchantments 添加到 Map 中
    public static void storeEnchantment(BlockPos blockPos, NbtList enchantments) {
        reverseMap.put(blockPos.toImmutable(), enchantments);
    }

    // 在需要时从 Map 中检索 NbtList enchantments
    public static NbtList getEnchantment(BlockPos blockPos) {
        return reverseMap.getOrDefault(blockPos.toImmutable(), new NbtList()); // 如果未找到 BlockPos，则返回空的 NbtList
    }

    public static int getLevel(Enchantment enchantment, BlockPos blockPos) {
        Identifier identifier = getEnchantmentId(enchantment);
        NbtList nbtList = getEnchantment(blockPos);

        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Identifier identifier2 = getIdFromNbt(nbtCompound);
            if (identifier2 != null && identifier2.equals(identifier)) {
                return getLevelFromNbt(nbtCompound);
            }
        }
        return 0;
    }
}
