package net.mafuyu33.mafishmod.block.custom;

import net.mafuyu33.mafishmod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class PotatoTNTPrepareBlock extends FlowerBlock {

    public PotatoTNTPrepareBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
        super(suspiciousStewEffect, effectDuration, settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        startDelayedOperation(world,pos,state,placer,itemStack);
    }

    private void startDelayedOperation(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        Timer timer = new Timer(); // 创建一个新的计时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                world.breakBlock(pos,false);
                world.setBlockState(pos, (ModBlocks.POTATO_TNT.getDefaultState()), 3);
            }
        }, 5000); // 延迟5秒执行，单位为毫秒
    }
}
