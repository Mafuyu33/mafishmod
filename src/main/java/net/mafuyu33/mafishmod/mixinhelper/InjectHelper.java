package net.mafuyu33.mafishmod.mixinhelper;


import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import java.util.Objects;

public class InjectHelper {

    public static void onPlacedInject(World world, ItemStack itemStack, BlockPos pos) {
        if (!world.isClient) {//只在服务端运行
            if (!Objects.equals(itemStack.getEnchantments(), new NbtList())) {//如果方块有附魔
                NbtList enchantments = itemStack.getEnchantments(); //获取物品栈上的附魔信息列表
                BlockEnchantmentStorage.addBlockEnchantment(pos.toImmutable(), enchantments);//储存信息
            }
        }
    }
}
