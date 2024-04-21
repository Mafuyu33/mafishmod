package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.bellresonance;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.mixinhelper.BellBlockDelayMixinHelper;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BellBlockEntity.class)
public abstract class BellBlockEntityMixin extends BlockEntity {
//    @Unique
//    private static final Map<BlockPos, Direction> delayedActivationPositions = new HashMap<>();
//
//    // 在适当的时候将实体ID和值添加到Map中
//    @Unique
//    private static void storeValue(BlockPos pos, Direction direction) {
//        delayedActivationPositions.put(pos, direction);
//    }
//    // 在需要时从Map中检索值
//    @Unique
//    private static Direction getDirection(BlockPos pos) {
//        return delayedActivationPositions.getOrDefault(pos, NORTH); // 默认值为NORTH
//    }
//    @Unique
//    private static boolean containsPos(BlockPos pos) {
//        return delayedActivationPositions.containsKey(pos);
//    }

    public BellBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(at = @At("HEAD"), method = "activate")
    private void init1(Direction direction, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(ModEnchantments.RESONANCE,pos);
        if(k>0){
            World world = getWorld();
            if (world != null && !world.isClient) {
            // 在当前方块位置周围 K+2 格范围内搜索其他的钟
                for (BlockPos nearbyPos : BlockPos.iterateOutwards(pos, k + 2, k + 2, k + 2)) {
                    // 排除当前位置
                    if (nearbyPos.equals(pos)) {
                        continue;
                    }
                    // 如果搜索到的方块是钟并且之前没有搜索过
                    if (world.getBlockState(nearbyPos).getBlock() instanceof BellBlock) {
                        // 激活找到的钟
                        BlockEntity nearbyBlockEntity = world.getBlockEntity(nearbyPos);
                        if (nearbyBlockEntity instanceof BellBlockEntity bellBlockEntity) {
                            if (!bellBlockEntity.ringing) {
                                BellBlockDelayMixinHelper.storeBellBlockEntity(pos,bellBlockEntity);
                                BellBlockDelayMixinHelper.storeDirection(pos,direction);
                                BellBlockDelayMixinHelper.storeHitCoolDown(pos,0);
//                                // 将找到的钟的位置添加到Map中，准备延迟激活
//                                storeValue(nearbyPos, direction);
//                                System.out.println("将找到的钟的位置添加到Map中，准备延迟激活" + delayedActivationPositions);
                            }
                        }
                    }
                }
            }
        }
    }
//    @Inject(at = @At("HEAD"), method = "tick")
//    private static void init2(World world, BlockPos pos, BlockState state, BellBlockEntity blockEntity, BellBlockEntity.Effect bellEffect, CallbackInfo ci) {
//        // 检查是否需要延迟激活钟
//        if (containsPos(pos)) {
//            System.out.println("激活钟");
//            // 激活钟
//            blockEntity.activate(getDirection(pos));
//            // 从数组中删除该位置
//            delayedActivationPositions.remove(pos);
//        }
//    }
}
