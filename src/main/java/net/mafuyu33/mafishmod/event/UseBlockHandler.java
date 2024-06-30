package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.mafuyu33.mafishmod.config.ConfigHelper;
import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UseBlockHandler implements UseBlockCallback {
    public static boolean isButtonUsed=false;

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient) {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());

            if (blockState.isIn(BlockTags.BUTTONS)) {
                // 检查按钮是否未被按下
                if (!blockState.get(Properties.POWERED)) {
                    isButtonUsed = true;
                }
            }
        }
        return ActionResult.PASS;
    }
}
