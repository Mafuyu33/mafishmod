package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AttackBlockHandler implements AttackBlockCallback {
    BlockPos startPos;
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        if(!world.isClient) {
            Iterable<ItemStack> handItemStacks = player.getHandItems();
            for (ItemStack itemstack : handItemStacks) {
                if (itemstack.isOf(Items.BRUSH)) {
                    if (itemstack.hasEnchantments()) {//有附魔，全图获取
                        if (startPos == null) {
                            startPos = pos;
                        } else {
                            brushAllBlocks(world, pos, itemstack);
                            startPos = null;
                        }
                    } else {//没附魔，清除附魔方块
                        if (startPos == null) {
                            startPos = pos;
                        } else {
                            clearAllBlocks(world, pos);
                            startPos = null;
                        }
                    }
                    return ActionResult.SUCCESS;
                }
            }
//        for (ItemStack itemstack : handItemStacks) {
//            if(itemstack.isOf(Items.BRUSH)){
//                return ActionResult.CONSUME;
//            }else {
//                return ActionResult.SUCCESS;
//            }
//        }
        }
        return ActionResult.PASS;
    }


    private void brushAllBlocks(World world, BlockPos pos, ItemStack itemStack) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), pos.getX());
        int minY = Math.min(startPos.getY(), pos.getY());
        int minZ = Math.min(startPos.getZ(), pos.getZ());
        int maxX = Math.max(startPos.getX(), pos.getX());
        int maxY = Math.max(startPos.getY(), pos.getY());
        int maxZ = Math.max(startPos.getZ(), pos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 排除空气、水、岩浆等特定方块
                    if (blockState.isOf(Blocks.AIR) ||
                            blockState.isOf(Blocks.WATER) ||
                            blockState.isOf(Blocks.LAVA)) {
                        continue;
                    }

                    // 在这里对满足条件的方块进行处理
                    BlockEnchantmentStorage.addBlockEnchantment(currentPos, itemStack.getEnchantments());
                    TutorialMod.LOGGER.info("Found block: " + blockState.getBlock().getTranslationKey() + " at " + currentPos);
                }
            }
        }
    }
    private void clearAllBlocks(World world, BlockPos pos) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), pos.getX());
        int minY = Math.min(startPos.getY(), pos.getY());
        int minZ = Math.min(startPos.getZ(), pos.getZ());
        int maxX = Math.max(startPos.getX(), pos.getX());
        int maxY = Math.max(startPos.getY(), pos.getY());
        int maxZ = Math.max(startPos.getZ(), pos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 在这里对满足条件的方块进行处理
                    BlockEnchantmentStorage.removeBlockEnchantment(currentPos);
                    TutorialMod.LOGGER.info("Found block: " + blockState.getBlock().getTranslationKey() + " at " + currentPos);
                }
            }
        }
    }
}
