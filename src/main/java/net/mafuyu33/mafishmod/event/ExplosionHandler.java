package net.mafuyu33.mafishmod.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ExplosionEvent;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplosionHandler {
    public static final Map<BlockPos, BlockState> protectedBlockStates = new HashMap<>();
    public static void init() {
        // 注册爆炸事件监听器
        ExplosionEvent.PRE.register(ExplosionHandler::onExplosionPre);
        ExplosionEvent.DETONATE.register(ExplosionHandler::onExplosionDetonate);
    }

    // 爆炸发生前的处理
    private static EventResult onExplosionPre(World world, Explosion explosion) {
        // 调用collectBlocksAndDamageEntities方法填充受影响方块列表
        explosion.collectBlocksAndDamageEntities();
        // 获取即将受到影响的方块坐标
        List<BlockPos> affectedBlocks = explosion.getAffectedBlocks();
        // 遍历受影响的方块
        for (BlockPos pos : affectedBlocks) {
            // 获取方块的附魔信息
            int blastProtectionLevel = BlockEnchantmentStorage.getLevel(Enchantments.BLAST_PROTECTION,pos);
            // 检查是否存在爆炸保护附魔
            if (blastProtectionLevel > 0) {
                // 存在爆炸保护附魔，阻止该方块被摧毁
                BlockState blockState = world.getBlockState(pos);
                System.out.println(blockState);
                protectedBlockStates.put(pos, blockState);
            }
        }

        // 允许爆炸继续执行
        return EventResult.pass();
    }


    // 爆炸发生后的处理
    private static void onExplosionDetonate(World world, Explosion explosion, List<Entity> entities) {
        // 在这里添加你的逻辑，处理爆炸发生后的事件
        // 还原受到爆炸保护的方块状态
        for (Map.Entry<BlockPos, BlockState> entry : protectedBlockStates.entrySet()) {
            System.out.println("还原受到爆炸保护的方块状态");
            BlockPos pos = entry.getKey();
            BlockState blockState = entry.getValue();
            world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
        }

        // 清除保存的受保护方块的状态，以便下一次爆炸事件
        protectedBlockStates.clear();
    }
}
